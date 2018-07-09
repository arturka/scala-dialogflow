package artur.dialogflow.services

import artur.dialogflow.interfaces.IntentHandler
import artur.dialogflow.utils.Configs
import artur.dialogflow.utils.LanguageCode.LanguageCode
import com.google.api.client.json.webtoken.JsonWebToken.Payload
import com.google.cloud.dialogflow.v2.Intent.{Message, TrainingPhrase}
import com.google.cloud.dialogflow.v2._
import com.google.protobuf.util.JsonFormat
import com.google.protobuf.{Struct, Value}

import scala.collection.JavaConverters._
import scala.util.Try

object IntentUploader {
  def apply(configs: Configs, intentsClient: IntentsClient): IntentUploader = new IntentUploader(configs, intentsClient)
}

class IntentUploader(configs: Configs, intentsClient: IntentsClient) extends IntentHandler {
  private val projectAgentName = ProjectAgentName.of(configs.getProjectId)

  override def batchIntentUpload(displayName: Option[String],
                                 actionName: Option[String],
                                 trainingPhrases: Option[Seq[String]],
                                 payload: Option[Struct],
                                 responseTexts: Option[Seq[String]],
                                 languageCode: LanguageCode): Try[Intent] = {
    val responses       = responseTexts.map(createResponses)
    val payloadMessage         = generatePayload(payload)
    val trainingParts   = trainingPhrases.map(list => list.map(createTrainingPhrase))
    val intent: Intent  = createIntent(displayName, actionName, responses, payloadMessage, trainingParts)

    Try {
      intentsClient.createIntent(projectAgentName, intent)
    }
  }

  private def generatePayload(payloadOpt: Option[Struct]) = payloadOpt match {
    case Some(payload) if payload.getFieldsCount > 0 => Some(Message.newBuilder.setPayload(payload).build())
    case _ => None
  }

  private def createResponses(responses: Seq[String]) = responses match {
    case list =>
      Message.newBuilder.setText(Message.Text.newBuilder.addAllText(list.asJava).build()).build()
    case _ => Message.newBuilder.build()
  }

  private def createTrainingPhrase(phrase: String) =
    Intent.TrainingPhrase.newBuilder
      .addParts(
        Intent.TrainingPhrase.Part.newBuilder
          .setText(phrase)
          .setUserDefined(true)
          .build()
      )
      .build()

  private def createIntent(displayName: Option[String],
                           actionName: Option[String],
                           message: Option[Message],
                           responsePayload: Option[Message],
                           trainingParts: Option[Seq[TrainingPhrase]]) = {

    def addTriggers(intent: Intent.Builder) = trainingParts match {
      case Some(triggers) => intent.addAllTrainingPhrases(triggers.asJava)
      case _              => intent
    }

    def addMessages(intent: Intent.Builder) = message match {
      case Some(messages) => intent.addMessages(messages)
      case _              => intent
    }

    def addAction(intent: Intent.Builder) = actionName match {
      case Some(action) if !action.isEmpty => intent.setAction(action)
      case _                               => intent
    }

    def addDisplayName(intent: Intent.Builder) = displayName match {
      case Some(name) => intent.setDisplayName(name)
      case _          => intent
    }

    def addResponsePayload(intent: Intent.Builder) = responsePayload match {
      case Some(payload) => intent.addMessages(payload)
      case _          => intent
    }

    Some(Intent.newBuilder)
      .map(addDisplayName)
      .map(addAction)
      .map(addMessages)
      .map(addTriggers)
      .map(addResponsePayload)
      .map(x => x.build())
      .get
  }

}
