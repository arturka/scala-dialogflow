import java.util.UUID

import artur.dialogflow.services.IntentUploader
import artur.dialogflow.utils.{BaseAgentConfigs, LanguageCode}
import com.google.cloud.dialogflow.v2._

object BootApp extends App {
  val sessionId      = UUID.randomUUID().toString
  val projectId      = System.getenv("PROJECT_ID")
  val intentsClient  = IntentsClient.create()
  val intentUploader = IntentUploader(BaseAgentConfigs(projectId), intentsClient)
  val response = intentUploader.batchIntentUpload("test.display.name",
                                                  "test.action.name",
                                                  List("Hello babe", "Hi", "Che tam?"),
                                                  List("Hello, hello", "How are you?"),
                                                  LanguageCode.English)
  println(response)
}
