package artur.dialogflow.services

import artur.dialogflow.interfaces.IntentHandler
import artur.dialogflow.utils.LanguageCode.LanguageCode
import com.google.cloud.dialogflow.v2.Intent
import com.google.cloud.dialogflow.v2.Intent.TrainingPhrase

import scala.util.{Success, Try}
import scala.collection.JavaConverters._

class IntentUploaderMock extends IntentHandler {
  override def batchIntentUpload(intentDisplayName: String,
                                 intentActionName: String,
                                 messagesText: Seq[String],
                                 languageCode: LanguageCode): Try[Intent] = {

    val messages = messagesText.map(
      text =>
        Intent.TrainingPhrase.newBuilder
          .addParts(
            TrainingPhrase.Part.newBuilder
              .setText(text)
              .build()
          )
          .build()
    )

    Success(
      Intent.newBuilder
        .setAction(intentActionName)
        .setDisplayName(intentDisplayName)
        .addAllTrainingPhrases(messages.asJavaCollection)
        .build()
    )
  }

  override def batchIntentUpload(intentDisplayName: String,
                                 intentActionName: String,
                                 messageText: String,
                                 languageCode: LanguageCode): Try[Intent] = ???
}
