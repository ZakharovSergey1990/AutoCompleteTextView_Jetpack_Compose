package ru.salvadorvdali.autocomletetextview_compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import ru.salvadorvdali.autocomletetextview_compose.ui.theme.AutoComleteTextView_ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutoComleteTextView_ComposeTheme {

                val text = remember{mutableStateOf("Some Text")}
                Surface(color = MaterialTheme.colors.background) {

Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TextFieldAutocomplete(list = listOf("this", "is", "some", "list")) {
                        text.value = it
                    }
Text(text = text.value, modifier = Modifier.padding(top = 50.dp), fontSize = 30.sp)
                    }

                }
            }
        }
    }
}


@Composable
fun TextFieldAutocomplete(
    list: List<String>,
    setValue: (String) -> Unit
){

    var text = remember { mutableStateOf("") }  // text in textfield
    var expanded = remember { mutableStateOf(false) } // for dropdownmenu
    var modifier: Modifier //
    Log.d("log", "text = ${text.value}")
    var  dropList= remember{ mutableStateOf(listOf(""))}
    dropList.value = list.filter { it.contains(text.value, ignoreCase = true)}

    Card(
        Modifier
            .padding(10.dp)
            .fillMaxWidth(), elevation = 10.dp, border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Some Title", modifier = Modifier
                    .padding(bottom = 5.dp)
            )
            Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                TextField(value = text.value,
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChange = {
                        text.value = it

                    })
                if (dropList.value.isNotEmpty() && text.value != "") {
                    modifier = if (dropList.value.size > 7) {
                        Modifier.height(300.dp)
                    } else {
                        Modifier
                    }

                    expanded.value = !(dropList.value.size == 1 && dropList.value[0]== text.value)

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false },
                        properties = PopupProperties(focusable = false),
                        modifier = modifier.fillMaxWidth()

                    ) {
                        dropList.value.forEach {
                            DropdownMenuItem(onClick = {
                                text.value = it
                                setValue(it)
                                dropList.value = emptyList()
                                expanded.value = false

                            }) {
                                Text(text = it)
                            }
                        }
                    }
                } else {
                    expanded.value = false
                }
            }
        }
    }
}

