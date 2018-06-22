import java.util.UUID

import com.google.cloud.dialogflow.v2._

object BootApp extends App {

  val sessionId = UUID.randomUUID().toString
  val projectId = System.getenv("PROJECT_ID")

  println(s"ProjectId = $projectId")

  val sessionClient: SessionsClient = SessionsClient.create()
  val session = SessionName.of(projectId, sessionId)
  val textInput = TextInput.newBuilder().setText("Hi").setLanguageCode("en-EN")
  val queryInput = QueryInput.newBuilder().setText(textInput).build()

  println(sessionClient.detectIntent(session, queryInput).getQueryResult.getFulfillmentMessagesList.get(0))
}
