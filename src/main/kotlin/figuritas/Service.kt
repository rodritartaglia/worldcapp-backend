package figuritas

interface MailSender {
    fun sendMail(mail : Mail)
}

data class Mail(
    val origin: String,
    val to: String,
    val title: String,
    val body: String
)