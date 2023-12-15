package com.chubasamuel.clinfind.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.chubasamuel.clinfind.R
import com.chubasamuel.clinfind.ui.theme.CFT

@Composable
fun AboutAppScreen(){
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                "ClinFind",
                fontSize = TextUnit(6f, TextUnitType.Em),
                fontWeight = FontWeight.Bold, color = CFT.colors.green
            )
            Text(
                "Find health facilities nearby",
                fontSize = TextUnit(3.5f, TextUnitType.Em),
                fontWeight = FontWeight.Bold, color = CFT.colors.optionsSel,
                fontStyle = FontStyle.Italic
            )

    Row(Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Image(ImageBitmap.imageResource(R.mipmap.clinfind_logo), null)
        Spacer(modifier = Modifier.width(10.dp))
        Text("ClinFind is more than just an app; it's your reliable companion on the journey to optimal health. Whether you're looking for a nearby hospital, clinic, pharmacy, laboratory, or specialized healthcare facility, ClinFind is designed to simplify the process of finding the right healthcare services tailored to your needs.",
            color= CFT.colors.textColor,
            fontSize = TextUnit(2.5f, TextUnitType.Em)
        )

    }
}}

@Preview
@Composable
fun PrevAbout(){
    AboutAppScreen()
}