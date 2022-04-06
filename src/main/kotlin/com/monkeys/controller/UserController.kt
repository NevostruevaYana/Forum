package com.monkeys.controller

import com.monkeys.*
import com.monkeys.models.Message
import com.monkeys.models.MessageModel
import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.lang.IllegalArgumentException
import java.sql.SQLException
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

class UserController {

    fun getHierarchy(name: String): Map<String, List<String>> {
        return try {
            val connection = getConnection()!!
            val res = TreeMap<String, List<String>>()
            val table = ArrayList<Pair<String, String>>()
            updateInactiveUsers(connection)
            updateLastActiveTime(connection, name)
                connection.from(MainThemeTable)
                    .innerJoin(SubThemeTable, on = MainThemeTable.id eq SubThemeTable.mainThemeId)
                    .select(MainThemeTable.themeName, SubThemeTable.themeName)
                    .orderBy(MainThemeTable.themeName.asc(), SubThemeTable.themeName.asc())
                    .map {
                        table.add(Pair(it[MainThemeTable.themeName].toString(), it[SubThemeTable.themeName].toString()))
                    }
                var main = table[0].first
                var sub = ArrayList<String>()
                table.forEach {
                    if (it.first == main) {
                        sub.add(it.second)
                    } else {
                        res[main] = sub
                        sub = ArrayList()
                        main = it.first
                        sub.add(it.second)
                    }
                }
                res[main] = sub
                res

        } catch (e: SQLException) {
            e.printStackTrace()
            throw SQLException("Something went wrong, please try again")
        }
    }

    fun getActiveUsers(name: String): List<String> {
        try {
            val connection = getConnection()!!
            val res = ArrayList<String>()
            updateInactiveUsers(connection)
            updateLastActiveTime(connection, name)
            connection.from(UserTable).select().where {
                UserTable.active eq true
            }.map { res.add(it[UserTable.name]!!) }
            return res
        } catch (e: SQLException) {
            e.printStackTrace()
            throw SQLException("Something went wrong, please try again")
        }
    }

    fun putNewMessage(name: String, msg: MessageModel): Boolean {
        try {
            val connection = getConnection()!!
            updateInactiveUsers(connection)
            updateLastActiveTime(connection, name)
            if (!checkIsAThemeExists(connection, msg.subTheme)) {
                throw IllegalArgumentException("No such sub theme found")
            }
            connection.insert(MessageTable) {
                set(it.text, msg.msg)
                set(it.userName, name)
                set(it.time, Instant.now())
                set(it.subTheme, msg.subTheme)
            }
            return true
        } catch (e: SQLException) {
            e.printStackTrace()
            throw SQLException("Something went wrong, please try again")
        }
    }

    fun getMessages(theme: String, name: String): List<Message> {
        try {
            val connection = getConnection()!!
            val res = ArrayList<Message>()
            updateInactiveUsers(connection)
            updateLastActiveTime(connection, name)
            if (!checkIsAThemeExists(connection, theme)) {
                throw IllegalArgumentException("No such sub theme found")
            }
            connection.from(MessageTable)
                .select(MessageTable.time, MessageTable.userName, MessageTable.text)
                .where {
                    MessageTable.subTheme eq theme
                }
                .map {
                    res.add(
                        Message(
                            it[MessageTable.time]!!.toString(),
                            it[MessageTable.userName]!!,
                            it[MessageTable.text]!!
                        )
                    )
                }
            return res
        } catch (e: SQLException) {
            e.printStackTrace()
            throw SQLException("Something went wrong, please try again")
        }
    }

    fun logout(name: String): Boolean {
        try {
            val connection = getConnection()!!
            updateInactiveUsers(connection)
            connection.update(UserTable) {
                set(it.active, false)
                where {
                    UserTable.name eq name
                }
            }
            return true
        } catch (e: SQLException) {
            e.printStackTrace()
            throw SQLException("Something went wrong, please try again")
        }
    }

    private fun updateInactiveUsers(connection: Database) {
        connection.update(UserTable) {
            set(it.active, false)
            where {
                UserTable.lastTimeOfActivity less Instant.now().minusSeconds(60 * 60)
            }
        }
    }

//    private fun checkActive(connection: Database, name: String): Boolean {
//        connection.from(UserTable).select().where {
//            UserTable.name eq name
//        }.forEach {
//            return it[UserTable.active].toString().toBoolean()
//        }
//        return false
//    }

    private fun updateLastActiveTime(connection: Database, name: String) {
        connection.update(UserTable) {
            set(it.active, true)
            set(it.lastTimeOfActivity, Instant.now())
            where {
                UserTable.name eq name
            }
        }
    }

    private fun checkIsAThemeExists(connection: Database, theme: String): Boolean {
        connection.from(SubThemeTable).select().where {
            SubThemeTable.themeName eq theme
        }.forEach {
            return true
        }
        return false
    }

}