package es.udc.psi.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;

import es.udc.psi.R;
import es.udc.psi.controller.impl.EditReserveControllerImpl;
import es.udc.psi.databinding.ActivityEditReserveBinding;
import es.udc.psi.model.Reserve;
import es.udc.psi.utils.CommonThings;
import es.udc.psi.view.interfaces.EditReserveView;

public class EditReserveActivity extends AppCompatActivity implements EditReserveView {

    private ActivityEditReserveBinding binding;
    private EditText passwordEditText;
    private TextInputLayout passwordTextInputLayout;
    private ToggleButton privatenessTButton;
    private Button modifyReserveButton;
    private EditReserveControllerImpl editReserveController;
    private Reserve toModifyReserve;

    /**
     * Se activa el campo "password" en función del valor del "toggle" de visibilidad
     */
    private void visibilityCongruence()
    {
        // True -> isPublic
        passwordEditText.setEnabled(!privatenessTButton.isChecked());
    }

    /**
     * @param value : Se pone el botón de visibilidad a "value" y se adecúa la visibilidad del
     *                campo "password" a ello. Si es "true", la reserva se desea pública y entonces
     *                la contraseña no tiene sentido, por tanto se desactiva
     */
    private void setVisibilityWithCongruence(boolean value)
    {
        privatenessTButton.setChecked(value);

        passwordEditText.setEnabled(!value);
    }

    /**
     * @param name
     * @param isPublic
     * @param password
     * @param capacity
     * @return true if any of the fields is distinct from the one gather from firebase
     */
    private boolean anyFieldDistinct(String name, Boolean isPublic, String password, int capacity)
    {
        /*
        Log.d("[DBG] 0 Edit Reserve: ", name);
        Log.d("[DBG] 0 Edit Reserve: ", isPublic.toString());
        Log.d("[DBG] 0 Edit Reserve: ", password);
        Log.d("[DBG] 0 Edit Reserve: ", String.valueOf(capacity));
        Log.d("[DBG] 1 Edit Reserve: ","name != reserve.name: " + String.valueOf(name != toModifyReserve.getName()));
        Log.d("[DBG] 1 Edit Reserve: ","isPublic != reserve.isPublic: " + String.valueOf(isPublic != toModifyReserve.isPublic()));
        Log.d("[DBG] 1 Edit Reserve: ","password != reserve.password: " + String.valueOf(password.compareTo(toModifyReserve.getPassword()) != 0));
        Log.d("[DBG] 1 Edit Reserve: ","capacity != reserve.capacity: " + String.valueOf(capacity != toModifyReserve.getCapacidadMax()));
        */

        return name.compareTo(toModifyReserve.getName()) != 0
                || isPublic != toModifyReserve.isPublic()
                || password.compareTo(toModifyReserve.getPassword()) != 0
                || capacity != toModifyReserve.getCapacidadMax()
        ;
    }


    private void updateUpdateButton() {
        boolean distinctFields = anyFieldDistinct(
                binding.reserveTitleField.getText().toString().trim(),
                privatenessTButton.isChecked(),
                passwordEditText.getText().toString().trim(),
                binding.ReserveMaxParticipantsPicker.getValue()
        );
        boolean allFieldsFilled = areAllFieldsFilled();
        modifyReserveButton.setEnabled(distinctFields && allFieldsFilled);
        updateBookButtonOpacity(distinctFields && allFieldsFilled);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditReserveBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        // Inicializa los controladores de registro y los repositorios (capa modelo)
        editReserveController = new EditReserveControllerImpl(this);

        // Recogiendo la reserva a editar del "intent"
        toModifyReserve = getIntent().getParcelableExtra(CommonThings.KEY_TO_MODIFY_RESERVE);
        System.out.println(toModifyReserve.toString());

        // Botón visibilidad
        privatenessTButton = findViewById(R.id.book_privateness_button);
        privatenessTButton.setEnabled(true);

        // Inicializa las variables de los elementos de la interfaz de usuario
        passwordEditText = findViewById(R.id.Book_passwordEditText);

        modifyReserveButton = binding.modifyReserveButton;

        passwordTextInputLayout = findViewById(R.id.Book_passwordTextInputLayout);

        binding.reserveTitleField.addTextChangedListener(fieldsTextWatcher);

        passwordEditText.addTextChangedListener(fieldsTextWatcher);

        // Setting default values
        setDefaultValues(toModifyReserve.getName(), toModifyReserve.isPublic(), toModifyReserve.getPassword(), toModifyReserve.getNumPlayers(), toModifyReserve.getCapacidadMax());

        // Reserve details are not modified yet
        modifyReserveButton.setEnabled(false);
        updateBookButtonOpacity(false);

        /************** Listeners ***************/

        // Configura el listener para el botón de registro
        modifyReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(binding.reserveTitleField.getText().toString());

                // Modifying the reserve
                toModifyReserve.setName(binding.reserveTitleField.getText().toString());
                toModifyReserve.setPublic(privatenessTButton.isChecked());
                toModifyReserve.setCapacidadMax(binding.ReserveMaxParticipantsPicker.getValue());
                toModifyReserve.setPassword(privatenessTButton.isChecked() ? "" : passwordEditText.getText().toString());

                editReserveController.validateAndUpdate(toModifyReserve);
            }
        });

        privatenessTButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                visibilityCongruence();
                updateUpdateButton();
            }
        });

    }

    private void setDefaultValues(String name, boolean aPublic, String password, int numPlayers, int maxCapacity) {
        binding.reserveTitleField.setText(name);
        setVisibilityWithCongruence(aPublic);
        passwordEditText.setText(password);
        binding.ReserveMaxParticipantsPicker.setMinValue(numPlayers);
        binding.ReserveMaxParticipantsPicker.setValue(maxCapacity);
        binding.ReserveMaxParticipantsPicker.setMaxValue(25);
    }

    // Métodos de la interfaz EditReserveView

    @Override
    public void onReserveChanged() {
        Toast.makeText(this, getString(R.string.Toast_ReserveModified), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(EditReserveActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onEditingReserveFailure(String errorMessage) {
        Toast.makeText(this, getString(R.string.Toast_FailedReserveModification) + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showValidationError(String fieldName, String errorMessage) {
        TextInputLayout inputLayout;
        switch (fieldName) {
            case "duracion":
                //inputLayout = emailTextInputLayout;
                break;
            case "password":
                inputLayout = passwordTextInputLayout;
                break;
            default:
                return;
        }
        // Línea temporal mientras no le damos un valor a "InputLayout" desde el caso "duracion"
        inputLayout = passwordTextInputLayout;
        inputLayout.setError(errorMessage);
        inputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.error_color));
    }

    @Override
    public void clearValidationError(String fieldName) {
        TextInputLayout inputLayout;
        switch (fieldName) {
            case "date":
                //inputLayout = emailTextInputLayout;
                break;
            case "password":
                inputLayout = passwordTextInputLayout;
                break;
            default:
                return;
        }
        // Línea temporal mientras no le damos un valor a "InputLayout" desde el caso "duracion"
        inputLayout = passwordTextInputLayout;
        inputLayout.setError(null);
        inputLayout.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.default_box_stroke_color));
    }

// TextWatcher y otros métodos auxiliares

    TextWatcher fieldsTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No es necesario implementar este método
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No es necesario implementar este método
        }

        @Override
        public void afterTextChanged(Editable s) {
            updateUpdateButton();
            /*
            boolean allFieldsFilled = areAllFieldsFilled();
            modifyReserveButton.setEnabled(allFieldsFilled);
            updateBookButtonOpacity(allFieldsFilled);
            */
        }
    };

    private boolean areAllFieldsFilled() {
        return  !TextUtils.isEmpty(binding.reserveTitleField.getText().toString().trim())
                && ( !TextUtils.isEmpty(passwordEditText.getText().toString().trim()) || privatenessTButton.isChecked() ); // If isPublic, then no password is needed;
    }

    private void updateBookButtonOpacity(boolean enabled) {
        float alpha = enabled ? 1.0f : 0.5f;
        modifyReserveButton.setAlpha(alpha);
    }

}