package artur.dialogflow.services

import artur.dialogflow.interfaces.IntentHandler
import artur.dialogflow.utils.Configs
import artur.dialogflow.utils.LanguageCode.LanguageCode
import com.google.cloud.dialogflow.v2.Intent.{Message, TrainingPhrase}
import com.google.cloud.dialogflow.v2.{Intent, IntentsClient, ProjectAgentName}
import com.google.protobuf.{Struct, Value}

import scala.collection.JavaConverters._
import scala.util.Try

object IntentUploader {
  def apply(configs: Configs, intentsClient: IntentsClient): IntentUploader = new IntentUploader(configs, intentsClient)
}

class IntentUploader(configs: Configs, intentsClient: IntentsClient) extends IntentHandler {
  private val projectAgentName = ProjectAgentName.of(configs.getProjectId)

  override def batchIntentUpload(displayName: String,
                                 actionName: String,
                                 trainingPhrases: Seq[String],
                                 responseTexts: Seq[String],
                                 languageCode: LanguageCode): Try[Intent] = {
    val responses: Message.Text = Message.Text.newBuilder.addAllText(responseTexts.asJava).build()
    val message: Message        = Message.newBuilder.setText(responses).build()
    val trainingParts           = trainingPhrases.map(generateTrainingPhrase)
    val intent = Intent.newBuilder
      .setDisplayName(displayName)
      .addAllTrainingPhrases(trainingParts.asJava)
      .setAction(actionName)
      .addMessages(message)
      .build()

    Try {
      intentsClient.createIntent(projectAgentName, intent)
    }
  }

  private def generateTrainingPhrase(text: String) = {
    val part = Intent.TrainingPhrase.Part.newBuilder
      .setText(text)
      .setUserDefined(true)
      .build()

    Intent.TrainingPhrase.newBuilder
      .addParts(part)
      .build
  }
}
