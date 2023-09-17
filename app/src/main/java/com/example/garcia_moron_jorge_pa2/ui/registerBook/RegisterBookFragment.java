package com.example.garcia_moron_jorge_pa2.ui.registerBook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.garcia_moron_jorge_pa2.databinding.FragmentRegisterBookBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterBookFragment extends Fragment {

    private FragmentRegisterBookBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RegisterBookViewModel registerBookViewModel =
                new ViewModelProvider(this).get(RegisterBookViewModel.class);

        binding = FragmentRegisterBookBinding.inflate(inflater, container, false);

        View view = inflater.inflate(com.example.garcia_moron_jorge_pa2.R.layout.fragment_register_book, container, false);

        //Guardando los datos del libro
        EditText title = (EditText) view.findViewById(R.id.editTextTitulo);
        EditText author = (EditText) view.findViewById(R.id.editTextAutor);
        EditText year = (EditText) view.findViewById(R.id.editTextDate);

        EditText searchText= (EditText) view.findViewById(R.id.searchEditText);

        Button register = (Button) view.findViewById(R.id.buttonRegister);
        Button search = (Button) view.findViewById(R.id.buttonSearch);
        Button clear = (Button) view.findViewById(R.id.buttonClear);
        Button deleteAll = (Button) view.findViewById(R.id.buttonDeleteAll);




        //Logica para registrar el libro
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleString = title.getText().toString();
                String authorString = author.getText().toString();
                String yearString = year.getText().toString();

                if (titleString.isEmpty()){
                    title.setError("Debe ingresar un titulo");
                    Toast.makeText(getActivity(), "Debe ingresar un titulo", Toast.LENGTH_SHORT).show();
                } else if (authorString.isEmpty()){
                    author.setError("Debe ingresar un autor");
                    Toast.makeText(getActivity(), "Debe ingresar un autor", Toast.LENGTH_SHORT).show();
                } else if (yearString.isEmpty()){
                    year.setError("Debe ingresar un año");
                    Toast.makeText(getActivity(), "Debe ingresar un año", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences preferences = getActivity().getSharedPreferences("books", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    // Obtener el número actual de libros registrados (si no hay ninguno, usar 0 como valor predeterminado)
                    int bookCount = preferences.getInt("book_count", 0);

                    // Crear un identificador único para el nuevo libro
                    String bookKey = "book_" + bookCount;

                    // Crear un objeto JSON para almacenar los valores del libro
                    JSONObject bookObject = new JSONObject();
                    try {
                        bookObject.put("title", titleString);
                        bookObject.put("author", authorString);
                        bookObject.put("year", yearString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Almacenar el objeto JSON bajo la clave única
                    editor.putString(bookKey, bookObject.toString());

                    // Incrementar el contador de libros registrados
                    editor.putInt("book_count", bookCount + 1);

                    editor.apply(); // Aplicar el cambio

                    Toast.makeText(getActivity(), "Libro registrado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Logica para buscar el libro
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("books", Context.MODE_PRIVATE);

                // Obtener el número actual de libros registrados
                int bookCount = preferences.getInt("book_count", 0);

                // Crear una cadena para mostrar los resultados de búsqueda
                StringBuilder searchResults = new StringBuilder();

                // Valor de búsqueda ingresado por el usuario
                String searchValue = searchText.getText().toString().toLowerCase(); // Obtener el valor y convertirlo a minúsculas para una búsqueda sin distinción entre mayúsculas y minúsculas

                for (int i = 0; i < bookCount; i++) {
                    String bookKey = "book_" + i;
                    String bookInfoJSON = preferences.getString(bookKey, "");

                    if (!bookInfoJSON.isEmpty()) {
                        try {
                            JSONObject bookObject = new JSONObject(bookInfoJSON);
                            String titleString = bookObject.getString("title").toLowerCase(); // Obtener el título y convertirlo a minúsculas
                            String authorString = bookObject.getString("author").toLowerCase(); // Obtener el autor y convertirlo a minúsculas
                            String yearString = bookObject.getString("year").toLowerCase(); // Obtener el año y convertirlo a minúsculas

                            // Comprobar si el valor de búsqueda coincide con el título, autor o año
                            if (titleString.contains(searchValue) || authorString.contains(searchValue) || yearString.contains(searchValue)) {
                                // Mostrar los resultados de búsqueda en los campos EditText
                                title.setText(titleString);
                                author.setText(authorString);
                                year.setText(yearString);

                                // Agregar una separación entre los resultados
                                searchResults.append("------ Libro ").append(i + 1).append(" ------\n");
                                searchResults.append("Título: ").append(titleString).append("\n");
                                searchResults.append("Autor: ").append(authorString).append("\n");
                                searchResults.append("Año: ").append(yearString).append("\n\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (searchResults.length() == 0) {
                    Toast.makeText(getActivity(), "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setText("");
                author.setText("");
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
                author.setText("");
                year.setText("");
                searchText.setText("");

                // Limpiar los valores almacenados en SharedPreferences
                SharedPreferences preferences = getActivity().getSharedPreferences("books", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                // Eliminar todas las entradas en SharedPreferences
                editor.clear();

                editor.apply(); // Aplicar el cambio1989

                // Mostrar un mensaje de confirmación o realizar otras acciones apropiadas
                Toast.makeText(getActivity(), "Todos los datos han sido eliminados", Toast.LENGTH_SHORT).show();
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