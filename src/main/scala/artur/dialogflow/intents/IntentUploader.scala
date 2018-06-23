package artur.dialogflow.intents

import artur.dialogflow.utils.Configs
import artur.dialogflow.utils.LanguageCode.LanguageCode
import com.google.cloud.dialogflow.v2.Intent.Message
import com.google.cloud.dialogflow.v2.{CreateIntentRequest, Intent, IntentsClient, ProjectAgentName}

import scala.collection.JavaConverters._
import scala.util.Try

class IntentUploader(configs: Configs) {

  private val projectAgentName = ProjectAgentName.of(configs.getProjectId)

  def batchTextUploader(displayName: String, messagesText: Seq[String], languageCode: LanguageCode): Try[Intent] = {
    val texts: Message.Text = Message.Text
      .newBuilder
      .addAllText(messagesText.asJavaCollection)
      .build()
    val message: Message = Message
      .newBuilder
      .setText(texts)
      .build()
    val intent = Intent
      .newBuilder
      .setDisplayName(displayName)
      .addMessages(message)
      .build()
    val intentclient = IntentsClient.create
    Try {
      intentclient.createIntent(projectAgentName, intent)
    }
  }
}