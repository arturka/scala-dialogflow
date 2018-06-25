package artur.dialogflow.interfaces

import artur.dialogflow.utils.LanguageCode.LanguageCode
import com.google.cloud.dialogflow.v2.Intent

import scala.util.Try

trait IntentHandler {

  def batchIntentUpload(displayName: String,
                        actionName: String,
                        trainingPhrases: Seq[String],
                        responseTexts: Seq[String],
                        languageCode: LanguageCode): Try[Intent]

  def batchIntentUpload(intentDisplayName: String,
                        intentActionName: String,
                        messageText: String,
                        languageCode: LanguageCode): Try[Intent]

}
