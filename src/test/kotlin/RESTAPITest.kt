import com.monkeys.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue


class RESTAPITest {
    private val httpClient: HttpClient = HttpClient(CIO) {
        expectSuccess = false
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    private fun getRandomString(len: Int): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..len)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    private val name = getRandomString(10)
    private val pwd = getRandomString(8)
    private val message = "Hello, world!"
    private val baseURL = "http://host.docker.internal:8080"
    private lateinit var subTheme: String
    private lateinit var token: String

    private fun register(): Unit = runBlocking{
        val response = httpClient.post<HttpResponse> {
            url("$baseURL/auth/sign-up")
            contentType(ContentType.Application.Json)
            body = AuthModel(name, pwd)
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    private fun login(): Unit = runBlocking{
        val response = httpClient.post<HttpResponse> {
            url("$baseURL/auth/sign-in")
            contentType(ContentType.Application.Json)
            body = AuthModel(name, pwd)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        token = response.receive<AuthResponse>().jwt
        assertNotNull(token)
        token = "Bearer $token"
    }

    private fun hierarchy(): Unit = runBlocking{
        val response = httpClient.get<HttpResponse> {
            headers {
                append(HttpHeaders.Authorization, token)
            }
            url("$baseURL/forum/request/hierarchy")
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val map = response.receive<HierarchyResponse>().hierarchy
        assertNotNull(map)
        assertTrue { map.isNotEmpty() }
        map.entries.forEach { subTheme = it.value[0] }
    }

    private fun active(): Unit = runBlocking{
        val response = httpClient.post<HttpResponse> {
            headers {
                append(HttpHeaders.Authorization, token)
            }
            url("$baseURL/forum/request/active-users")
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull(response.receive<ActivityUsersResponse>().users)
    }

    private fun msg(): Unit = runBlocking{
        val response = httpClient.post<HttpResponse> {
            headers {
                append(HttpHeaders.Authorization, token)
            }
            url("$baseURL/forum/request/message")
            contentType(ContentType.Application.Json)
            body = MessageModel(subTheme, message)
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    private fun msgList(): Unit = runBlocking{
        val response = httpClient.get<HttpResponse> {
            headers {
                append(HttpHeaders.Authorization, token)
            }
            url("$baseURL/forum/request/message-list/$subTheme")
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    private fun logout(): Unit = runBlocking{
        val response = httpClient.delete<HttpResponse> {
            headers {
                append(HttpHeaders.Authorization, token)
            }
            url("$baseURL/forum/request/logout")
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        httpClient.close()
    }

    @Test
    fun runTests() {
        register()
        println("register ok")
        login()
        println("login ok")
        hierarchy()
        println("hierarchy ok")
        active()
        println("active ok")
        msg()
        println("msg ok")
        msgList()
        println("msgList ok")
        logout()
        println("logout ok")
    }

}