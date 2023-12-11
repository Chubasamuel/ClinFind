package com.chubasamuel.clinfind.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.chubasamuel.clinfind.data.local.Facility
import com.chubasamuel.clinfind.data.local.FilterSearch
import com.chubasamuel.clinfind.data.local.FilterSearchBy
import com.chubasamuel.clinfind.data.local.Search

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchComp(onSearch:(Search)->Unit, filterSearch: FilterSearch){
    var searchText by remember { mutableStateOf("") }
    val searchByList = listOf("name", "direction", "owner", "about")
    var selectedSearchBy by remember { mutableStateOf(searchByList[0]) }
    var search by remember{ mutableStateOf(Search(selectedSearchBy,searchText))}
    val filters = FilterSearchBy.values()
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)) {
        val containerColor = Color.LightGray
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){ TextField(value = searchText, onValueChange = { s->searchText=s}, placeholder = {Text("Search...")},
            colors= TextFieldDefaults.colors(
                disabledTextColor = Color.Transparent,
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            leadingIcon = {Icon(Icons.Filled.Search,null)},
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier.fillMaxWidth()
            )}
        Spacer(Modifier.height(8.dp))
        SearchByDropdown(selected = selectedSearchBy,li = searchByList, onSelect = {s->selectedSearchBy=s})
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly){
            for( f in filters){
                val liData = when(f){
                    FilterSearchBy.lga->{filterSearch.lgas}
                    FilterSearchBy.state->{filterSearch.states}
                    FilterSearchBy.specialty->{filterSearch.specialties}
                    FilterSearchBy.type->{filterSearch.types}
                }
                FilterDropdown(search = search,by = f, li =liData ,
                    onSelect ={fb,s->search=search.mutateFilterBy(fb,s)} )
            }
        }
        Spacer(Modifier.height(10.dp))
        Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(color=Color.Black))
    }
}
fun Search.mutateFilterBy(fb:FilterSearchBy,value:String):Search{
    return when(fb){
        FilterSearchBy.lga->{this.copy(lga=value)}
        FilterSearchBy.state->{this.copy(state=value)}
        FilterSearchBy.specialty->{this.copy(specialty=value)}
        FilterSearchBy.type->{this.copy(type=value)}
    }
}
@Composable
fun SearchByDropdown(selected:String,li:List<String>, onSelect:(String)->Unit){
    var expanded by remember { mutableStateOf(false) }
    Column{
        Row{
            Text("Search by($selected)", Modifier.clickable { expanded=!expanded })
            Icon(Icons.Filled.MoreVert,null, Modifier.clickable { expanded=!expanded })
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded=false }) {
            for(s in li){
                DropdownMenuItem(text = { Text(s) }, onClick = { expanded=false; onSelect(s) })
            }
        }
    }
}
@Composable
fun FilterDropdown(search:Search,by:FilterSearchBy, li:List<String>, onSelect:(FilterSearchBy,String)->Unit){
    var expanded by remember { mutableStateOf(false) }
    val ss = when(search.type){
        by.label->search.javaClass.getDeclaredField(by.name).get(search) as? String
        else->null
    }
    Log.w("DCOR DEBUG","ss-->$ss")
    Column{
        Row{Text(by.label,Modifier.clickable { expanded=!expanded })
        ss?.let {
            Text(ss,Modifier.clickable { expanded=!expanded })
        }
        Icon(Icons.Filled.ArrowDropDown,null,Modifier.clickable { expanded=!expanded })
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded=false }) {
            for(s in li){
                DropdownMenuItem(text = { Text(s) }, onClick = { expanded=false; onSelect(by,s) })
            }
        }
    }
}

@Composable
fun FacilityComp(f:Facility){
    Column(
        Modifier
            .fillMaxWidth()
            //.padding(5.dp)
            .background(color = Color.LightGray, shape = RoundedCornerShape(15.dp))
            .padding(10.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text=f.name, modifier = Modifier
                .weight(0.7f)
                .padding(end = 5.dp))
            f.type?.let{ElevatedButton(onClick = { /*TODO*/ }, modifier = Modifier.weight(0.3f)){
                Text(f.type, fontSize = TextUnit(3f, TextUnitType.Em))
            }}
        }
        Row {
            Text(f.about?.take(100)?:"")
        }
        //Row {
            Text(f.specialty)
            Row {
              Icon(Icons.Filled.LocationOn, contentDescription =null )
                Spacer(Modifier.width(5.dp))
                Text("${f.lga}, ${f.state}")
            }
      //  }
    }
}