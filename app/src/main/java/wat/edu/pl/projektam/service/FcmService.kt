package wat.edu.pl.projektam.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import wat.edu.pl.projektam.MainActivity
import wat.edu.pl.projektam.R
import wat.edu.pl.projektam.data.remote.api.AuthApiService
import wat.edu.pl.projektam.data.remote.dto.FcmTokenRequest
import wat.edu.pl.projektam.data.local.preferences.TokenManager
import wat.edu.pl.projektam.util.Constants
import javax.inject.Inject

@AndroidEntryPoint
class FcmService : FirebaseMessagingService() {

    @Inject lateinit var authApiService: AuthApiService
    @Inject lateinit var tokenManager: TokenManager

    // Wywołane gdy Firebase przydziela lub odświeża token urządzenia
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        if (tokenManager.isLoggedIn()) {
            CoroutineScope(Dispatchers.IO).launch {
                runCatching { authApiService.updateFcmToken(FcmTokenRequest(token)) }
            }
        }
    }

    // Wywołane gdy przychodzi powiadomienie push
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title ?: message.data["title"] ?: return
        val body  = message.notification?.body  ?: message.data["body"]  ?: return
        showNotification(title, body)
    }

    private fun showNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_PUSH)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
