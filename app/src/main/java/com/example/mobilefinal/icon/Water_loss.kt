package com.example.mobilefinal.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Water_loss: ImageVector
	get() {
		if (_Water_loss != null) {
			return _Water_loss!!
		}
		_Water_loss = ImageVector.Builder(
            name = "Water_loss",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
			path(
    			fill = SolidColor(Color.Black),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(444f, 640f)
				quadToRelative(-47f, 0f, -91.5f, 11.5f)
				reflectiveQuadTo(267f, 685f)
				lineToRelative(13f, 115f)
				horizontalLineToRelative(400f)
				lineToRelative(13f, -120f)
				quadToRelative(-36f, 0f, -67f, -6f)
				reflectiveQuadToRelative(-84f, -21f)
				quadToRelative(-23f, -7f, -48f, -10f)
				reflectiveQuadToRelative(-50f, -3f)
				moveToRelative(-186f, -40f)
				quadToRelative(45f, -20f, 92f, -30f)
				reflectiveQuadToRelative(95f, -10f)
				quadToRelative(30f, 0f, 59.5f, 4f)
				reflectiveQuadToRelative(58.5f, 12f)
				quadToRelative(50f, 14f, 76.5f, 19f)
				reflectiveQuadToRelative(56.5f, 5f)
				horizontalLineToRelative(6f)
				lineToRelative(48f, -440f)
				horizontalLineTo(210f)
				close()
				moveToRelative(21f, 280f)
				quadToRelative(-31f, 0f, -53.5f, -20f)
				reflectiveQuadTo(200f, 809f)
				lineToRelative(-80f, -729f)
				horizontalLineToRelative(720f)
				lineToRelative(-80f, 729f)
				quadToRelative(-3f, 31f, -25.5f, 51f)
				reflectiveQuadTo(681f, 880f)
				close()
				moveToRelative(165f, -80f)
				horizontalLineToRelative(236f)
				horizontalLineToRelative(-400f)
				close()
			}
		}.build()
		return _Water_loss!!
	}

private var _Water_loss: ImageVector? = null
