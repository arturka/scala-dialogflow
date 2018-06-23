import java.util.UUID

import com.google.cloud.dialogflow.v2.Intent.Message
import com.google.cloud.dialogflow.v2._

object BootApp extends App {

  val sessionId = UUID.randomUUID().toString
  val projectId = System.getenv("PROJECT_ID")

//  println(s"ProjectId = $projectId")
//
//  val sessionClient: SessionsClient = SessionsClient.create()
//  val session = SessionName.of(projectId, sessionId)
//  val textInput = TextInput.newBuilder().setText("Hi").setLanguageCode("en-EN")
//  val queryInput = QueryInput.newBuilder().setText(textInput).build()

  val projectAgentName = ProjectAgentName.of(projectId)

  val text = Message
    .Text
    .newBuilder
    .addText("Hello")
    .build()

  val message = Message
    .newBuilder
    .setText(text)
    .build()

  val intent = Intent
      .newBuilder
      .setDisplayName("Test - intent")
      .setAction(null)
      .addMessages(message)
      .build()

  val createIntentRequest = CreateIntentRequest
    .newBuilder
    .setIntent(intent)
    .setLanguageCode("en-EN")
    .setParent(projectId)
    .build()

  val result = IntentsClient.create().createIntent(projectAgentName, intent)
  println(result)

//  println(sessionClient.detectIntent(session, queryInput))
}
