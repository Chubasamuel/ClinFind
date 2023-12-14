package com.chubasamuel.clinfind.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.chubasamuel.clinfind.R
import com.chubasamuel.clinfind.data.local.Facility
import com.chubasamuel.clinfind.data.local.FilterSearch
import com.chubasamuel.clinfind.data.local.FilterSearchBy
import com.chubasamuel.clinfind.data.local.Search
import com.chubasamuel.clinfind.ui.theme.CFT
import com.chubasamuel.clinfind.ui.theme.alertBgShape
import com.chubasamuel.clinfind.util.Links
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchComp(onSearch:(Search)->Unit, filterSearch: FilterSearch, goToAbout:()->Unit){
    var searchText by remember { mutableStateOf("") }
    val searchByList = listOf("name", "direction", "owner", "about")
    var selectedSearchBy by remember { mutableStateOf(searchByList[0]) }
    var search by remember{ mutableStateOf(Search(selectedSearchBy,searchText))}
    var revealFilters by remember { mutableStateOf(false) }
    val filters = FilterSearchBy.values()
    var job:Job? = null
    val context = LocalContext.current
    fun triggerSearch(){
        job?.cancel()
        job= CoroutineScope(Dispatchers.IO).launch {
            delay(1500)
            onSearch(search)
        }
    }
    fun onMenuSelect(sel:Int){
        when(sel){
            0->{addLocation(context)}
            1->{goToAbout()}
            2->{launchPlayStore(context) }
            3->{contactDev(context)}
        }
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)) {
        val containerColor = CFT.colors.cardBg
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
            TextField(value = searchText, onValueChange = {
                    s->searchText=s
                search=search.copy(paramValue = searchText)
                triggerSearch()
                                                          },
                placeholder = {Text("Search...", color=CFT.colors.textColor)},
            colors= TextFieldDefaults.colors(
                disabledTextColor = Color.Transparent,
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = CFT.colors.textColor
            ),
            leadingIcon = {Icon(Icons.Filled.Search,null, tint = CFT.colors.searchIconTint)},
            trailingIcon= {
                var expanded by remember { mutableStateOf(false) }
                LogoMenu(expanded, {e->expanded=!e},
                listOf("Add location","About", "Rate app","Contact support"), {i->onMenuSelect(i)}
            )},
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier.fillMaxWidth()
            )}
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
           IconButton(onClick = { revealFilters=!revealFilters }) {

           Icon(if(revealFilters)
                Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown, null, tint=CFT.colors.searchIconTint)
        }}
        AnimatedVisibility(visible = revealFilters) {
            Spacer(Modifier.height(8.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(color = CFT.colors.cardBg, shape = alertBgShape)
                    .padding(15.dp)){
            SearchByDropdown(
                selected = selectedSearchBy,
                li = searchByList,
                onSelect = {
                        s -> selectedSearchBy = s
                    search = search.copy(param=selectedSearchBy)
                    triggerSearch()
                })
            Spacer(Modifier.height(8.dp))
            Text("Filters:", color=CFT.colors.textColor)
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                for (f in filters) {
                    val liData = when (f) {
                        FilterSearchBy.lga -> {
                            filterSearch.lgas
                        }

                        FilterSearchBy.state -> {
                            filterSearch.states
                        }

                        FilterSearchBy.specialty -> {
                            filterSearch.specialties
                        }

                        FilterSearchBy.type -> {
                            filterSearch.types
                        }
                    }
                    FilterDropdown(search = search, by = f, li = liData,
                        onSelect = { fb, s -> search = search.mutateFilterBy(fb, s); triggerSearch() })
                }
            }
            Spacer(Modifier.height(10.dp))
            Spacer(modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = CFT.colors.searchIconTint))
        }}
    }
}
@Composable
fun LogoMenu(expanded:Boolean=false, onLogoClick:(Boolean)->Unit,li:List<String>, onSelect:(Int)->Unit){
    Image(ImageBitmap.imageResource(R.mipmap.clinfind_logo), null, Modifier.clickable { onLogoClick(expanded) })
    DropdownMenu(expanded = expanded, onDismissRequest = { onLogoClick(true) }) {
        for(i in li.indices){
            DropdownMenuItem(text = { Text(li[i]) }, onClick = { onLogoClick(true); onSelect(i) })
        }
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
            Text("Search by($selected)", Modifier.clickable { expanded=!expanded }, color=CFT.colors.textColor)
            Icon(Icons.Filled.MoreVert,null, Modifier.clickable { expanded=!expanded }, tint=CFT.colors.searchIconTint)
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
    val ss = when(by){
        FilterSearchBy.lga->search.lga
        FilterSearchBy.type->search.type
        FilterSearchBy.specialty->search.specialty
        FilterSearchBy.state->search.state
    }

    Column{
        Row{Text(by.label,Modifier.clickable { expanded=!expanded }, color=CFT.colors.textColor)
        Icon(Icons.Filled.ArrowDropDown,null,Modifier.clickable { expanded=!expanded }, tint=CFT.colors.searchIconTint)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded=false }) {
            for(s in li){
                DropdownMenuItem(text = {
                    if(s==ss){Text(s,color=CFT.colors.primary)}
                        else {Text(s) }}, onClick = { expanded=false; onSelect(by,s) })
            }
        }
    }
}

@Composable
fun FacilityComp(f:Facility){
    var revealAddress by remember { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxWidth()
            .background(color = CFT.colors.cardBg, shape = RoundedCornerShape(15.dp))
            .padding(15.dp)) {

        /*Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End){f.type?.let{ElevatedButton(onClick = { /*TODO*/ }, /*modifier = Modifier.weight(0.3f)*/){
            Text(f.type, fontSize = TextUnit(3f, TextUnitType.Em))
        }}}*/
            Text(text=f.name, modifier = Modifier
                .padding(end = 5.dp), color = CFT.colors.textColor, fontWeight = FontWeight.Bold ,fontSize = TextUnit(6f, TextUnitType.Em))
        Column {
            Text(f.about?.take(100)?:"",
                modifier=Modifier.padding(horizontal = 10.dp),
                fontStyle = FontStyle.Italic, color = CFT.colors.textColor)
            Spacer(Modifier.height(10.dp))
        }

            Row{
                Icon(Icons.Filled.Favorite,null, tint=CFT.colors.red)
                Spacer(Modifier.width(5.dp))
                Text(f.specialty,fontSize = TextUnit(4f, TextUnitType.Em), color=CFT.colors.textColor)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                AnimatedVisibility(visible = !revealAddress) {
                    Row{Icon(Icons.Filled.LocationOn, contentDescription =null, tint=CFT.colors.green)
                    Spacer(Modifier.width(5.dp))
                    Text("${f.lga}, ${f.state}", color=CFT.colors.textColor)}
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { revealAddress=!revealAddress}) {
                Icon(if(revealAddress)
                    Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown, contentDescription =null, tint=CFT.colors.searchIconTint )
            }}
        AnimatedVisibility(visible = revealAddress) {
            Column(Modifier.fillMaxWidth()){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.LocationOn, contentDescription =null,tint=CFT.colors.green )
                Spacer(Modifier.width(5.dp))
                Column{
                    Text(f.direction?:"[Direction not specified]", color=CFT.colors.textColor)
                    Text("${f.lga}, ${f.state}", color = CFT.colors.textColor)}}
            Row(verticalAlignment = Alignment.CenterVertically) {
                val contacts by remember { derivedStateOf { if(f.contact.isNullOrEmpty()){null}else{f.contact.joinToString(separator = ", ")} }}
                Icon(Icons.Filled.Phone, contentDescription =null, tint=CFT.colors.blue )
                Spacer(Modifier.width(5.dp))
                SelectionContainer {
                    Text(contacts ?: "[Contacts not specified]", color = CFT.colors.textColor)
                }}
        }}
    }
}

@Composable
fun ErrorScreenComp(retryFetch:()->Unit){
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Row{
            Icon(Icons.Filled.Warning,null,tint= CFT.colors.red)
            Spacer(Modifier.width(15.dp))
            Text("Error occurred while fetching data over the internet.\n" +
                    "Please check your network and try again.")
        }
        Spacer(Modifier.height(40.dp))
        Button(onClick = { retryFetch() }) {
            Icon(Icons.Filled.Refresh,null,tint=CFT.colors.searchIconTint)
            Spacer(Modifier.width(5.dp))
            Text("Retry")
        }
    }
}

private fun contactDev(context:Context){
    val mailto="mailto:dev.chubasamuel@gmail.com?"+"subject="+ Uri.encode("Feedback on ClinFind Android app")+
            "&body="+Uri.encode(" ")
    val intent=Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse(mailto)
    context.startActivity(intent)
}
private fun launchPlayStore(context: Context){
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data =
        Uri.parse("http://play.google.com/store/apps/details?id=com.chubasamuel.clinfind")
    val chooser = Intent.createChooser(intent, "launch Play store")
    context.startActivity(chooser)
}

private fun addLocation(context: Context){
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Links.addLocation)))
}