package murrieta.sebastian.practicaautenticacionmurrietas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvError: TextView
    private lateinit var btnLogin: Button
    private lateinit var btnGoRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        tvError = findViewById(R.id.tvError)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoRegister = findViewById(R.id.btnGoRegister)

        tvError.visibility = View.INVISIBLE

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showError("Por favor, llena todos los campos.")
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        btnGoRegister.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            goToMainActivity(currentUser.email)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    goToMainActivity(user?.email)
                } else {
                    // If sign in fails, display a message to the user.
                    showError("Autenticación fallida: ${task.exception?.message}")
                }
            }
    }

    private fun goToMainActivity(email: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("user", email)
        }
        startActivity(intent)
        finish()
    }

    private fun showError(message: String, isError: Boolean = true) {
        tvError.text = message
        tvError.visibility = if (isError) View.VISIBLE else View.INVISIBLE
    }
}
