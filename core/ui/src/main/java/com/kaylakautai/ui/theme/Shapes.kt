package com.kaylakautai.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object ShapeCornerRadii {
    val small = 12.dp
    val medium = 20.dp
    val large = 50.dp
}

val PikauShapes = Shapes(
    small = RoundedCornerShape(ShapeCornerRadii.small),
    medium = RoundedCornerShape(ShapeCornerRadii.medium),
    large = RoundedCornerShape(ShapeCornerRadii.large),
)