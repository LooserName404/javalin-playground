import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.util.FileUtil
import io.javalin.http.Context
import java.lang.Exception
import java.util.*
import app.HelloPage
import app.Localizer

val reservations = mutableMapOf<String?, String?>(
    "saturday" to "No reservation",
    "sunday" to "No reservation"
)

object App {

    @JvmStatic
    fun main(args: Array<String>) {

        val userDao = UserDao()

        val app = Javalin.create {
            it.addStaticFiles("/public")
            it.enableWebjars()
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("not found") }
        }.start(3000)

        app.routes {
            get("/users") { ctx ->
                ctx.json(userDao.users)
            }

            get("/users/:user-id") { ctx ->
                ctx.json(userDao.findById(ctx.pathParam("user-id").toInt())!!)
            }

            get("/users/email/:email") { ctx ->
                ctx.json(userDao.findByEmail(ctx.pathParam("email")) ?: "not found")
            }

            post("/users") { ctx ->
                val user = ctx.body<User>()
                userDao.save(name = user.name, email = user.email)
                ctx.status(201)
            }

            patch("/users/:user-id") { ctx ->
                val user = ctx.body<User>()
                userDao.update(
                    id = ctx.pathParam("user-id").toInt(),
                    user = user
                )
            }

            delete("/users/:user-id") { ctx ->
                userDao.delete(ctx.pathParam("user-id").toInt())
                ctx.status(204)
            }

            post("/make-reservation") { ctx ->
                reservations[ctx.formParam("day")] = ctx.formParam("time")
                ctx.html("Your reservation has been saved")
            }

            get("/check-reservation") { ctx ->
                ctx.html(reservations[ctx.queryParam("day")] ?: "Invalid day informed")
            }

            post("/upload-example") { ctx ->
                ctx.uploadedFiles("files").forEach {
                    FileUtil.streamToFile(it.content, "upload/${it.filename}")
                }
                ctx.html("Upload complete")
            }

            get("/jte", this::renderHelloPage)
        }
    }

    private fun renderHelloPage(ctx: Context) {
        val page = HelloPage()
        page.userName = "<script>alert('xss')</script>"
        page.userKarma = 1337
        ctx.render("hello.jte", mapOf("page" to page, "localizer" to Localizer(Locale.US)))
    }
}

