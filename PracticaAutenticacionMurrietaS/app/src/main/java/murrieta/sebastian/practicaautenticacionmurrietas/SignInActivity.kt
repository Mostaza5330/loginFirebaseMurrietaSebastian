package murrieta.sebastian.practicaautenticacionmurrietas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etrEmail: EditText
    private lateinit var etrPassword: EditText
    private lateinit var etrConfirmPassword: EditText
    private lateinit var tvrError: TextView
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth

        etrEmail = findViewById(R.id.etrEmail)
        etrPassword = findViewById(R.id.etrPassword)
        etrConfirmPassword = findViewById(R.id.etrConfirmPassword)
        tvrError = findViewById(R.id.tvrError)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val email = etrEmail.text.toString()
            val password = etrPassword.text.toString()
            val confirmPassword = etrConfirmPassword.text.toString()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showError("Todos los campos deben estar llenos.")
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                showError("Las contraseñas no coinciden.")
                return@setOnClickListener
            }

            registerUser(email, password)
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    showError("Registro exitoso.", false)
                    // Optionally navigate to another activity or clear fields
                } else {
                    // If sign in fails, display a message to the user.
                    showError("Error de registro: ${task.exception?.message}")
                }
            }
    }

    private fun showError(message: String, isError: Boolean = true) {
        tvrError.text = message
        tvrError.visibility = if (isError) View.VISIBLE else View.INVISIBLE
    }
}
