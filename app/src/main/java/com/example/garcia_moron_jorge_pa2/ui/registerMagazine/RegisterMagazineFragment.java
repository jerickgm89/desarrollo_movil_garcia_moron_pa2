package com.example.garcia_moron_jorge_pa2.ui.registerMagazine;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.garcia_moron_jorge_pa2.R;
import com.example.garcia_moron_jorge_pa2.databinding.FragmentRegisterMagazineBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class RegisterMagazineFragment extends Fragment {

    private FragmentRegisterMagazineBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RegisterMagazineViewModel registerMagazineViewModel =
                new ViewModelProvider(this).get(RegisterMagazineViewModel.class);

        binding = FragmentRegisterMagazineBinding.inflate(inflater, container, false);

        View view = inflater.inflate(R.layout.fragment_register_magazine, container, false);

        EditText title = (EditText) view.findViewById(R.id.editTextTitleMagazine);
        EditText editor = (EditText) view.findViewById(R.id.editTextEditorial);
        EditText year = (EditText) view.findViewById(R.id.editTextYearMagazine);

        EditText searchText= (EditText) view.findViewById(R.id.editTextSearchMaga);

        Button register = (Button) view.findViewById(R.id.buttonRegisterMaga);
        Button search = (Button) view.findViewById(R.id.buttonSearchMaga);
        Button clear = (Button) view.findViewById(R.id.buttonClearMaga);
        Button deleteAll = (Button) view.findViewById(R.id.buttonDeleteAllMaga);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleString = title.getText().toString();
                String editorString = editor.getText().toString();
                String yearString = year.getText().toString();

                if (titleString.isEmpty() || editorString.isEmpty() || yearString.isEmpty()) {
                    Toast.makeText(getActivity(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Crear una cadena con los datos de la revista a guardar en el archivo
                String dataToSave = "Título: " + titleString + "\n" +
                        "Editor: " + editorString + "\n" +
                        "Año: " + yearString + "\n\n"; // Agregar un salto de línea entre las revistas

                // Obtener la ruta del directorio externo público donde se guardará el archivo
                File externalDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File file = new File(externalDir, "revistas.txt");

                try {
                    // Crear o abrir el archivo en modo de adjuntar (para agregar datos a revistas existentes)
                    FileOutputStream outputStream = new FileOutputStream(file, true); // El segundo parámetro "true" indica modo de adjuntar

                    // Escribir los datos en el archivo
                    outputStream.write(dataToSave.getBytes());

                    // Cerrar el flujo de salida
                    outputStream.close();

                    Toast.makeText(getActivity(), "Revista registrada y guardada en " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error al guardar la revista", Toast.LENGTH_SHORT).show();
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el término de búsqueda ingresado
                String searchTerm = searchText.getText().toString().toLowerCase(); // Convertir a minúsculas para una búsqueda sin distinción entre mayúsculas y minúsculas

                // Obtener la ruta del archivo de texto donde se guardaron las revistas
                File externalDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File file = new File(externalDir, "revistas.txt");

                if (!file.exists()) {
                    Toast.makeText(getActivity(), "No se encontraron datos de revistas", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Crear variables para almacenar los resultados de búsqueda
                String foundTitle = "";
                String foundEditor = "";
                String foundYear = "";

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        // Verificar si la línea contiene el término de búsqueda
                        if (line.toLowerCase().contains(searchTerm)) {
                            // Dividir la línea en partes (asumiendo que los datos están separados por ":")
                            String[] parts = line.split(":");

                            if (parts.length == 2) {
                                String key = parts[0].trim();
                                String value = parts[1].trim();

                                if (key.equalsIgnoreCase("Título")) {
                                    foundTitle = value;
                                } else if (key.equalsIgnoreCase("Editor")) {
                                    foundEditor = value;
                                } else if (key.equalsIgnoreCase("Año")) {
                                    foundYear = value;
                                }
                            }
                        }
                    }

                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error al leer el archivo", Toast.LENGTH_SHORT).show();
                }

                // Mostrar los resultados de búsqueda en los campos EditText correspondientes
                title.setText(foundTitle);
                editor.setText(foundEditor);
                year.setText(foundYear.toLowerCase());

                if (foundTitle.isEmpty() && foundEditor.isEmpty() && foundYear.isEmpty()) {
                    Toast.makeText(getActivity(), "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setText("");
                editor.setText("");
                year.setText("");
                searchText.setText("");

                Toast.makeText(getActivity(), "Campos de búsqueda limpiados", Toast.LENGTH_SHORT).show();
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limpiar los campos de búsqueda (EditText)
                title.setText("");
                editor.setText("");
                year.setText("");
                searchText.setText("");

                // Obtener la ruta del archivo de texto donde se guardaron las revistas
                File externalDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File file = new File(externalDir, "revistas.txt");

                if (!file.exists()) {
                    Toast.makeText(getActivity(), "No se encontraron datos de revistas", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Eliminar el archivo
                if (file.delete()) {
                    Toast.makeText(getActivity(), "Datos de revistas eliminados", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error al eliminar los datos de revistas", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}