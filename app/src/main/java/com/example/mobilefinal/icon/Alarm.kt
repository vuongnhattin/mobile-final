package com.example.mobilefinal.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Alarm: ImageVector
	get() {
		if (_Alarm != null) {
			return _Alarm!!
		}
		_Alarm = ImageVector.Builder(
            name = "Alarm",
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
				moveTo(480f, 880f)
				quadToRelative(-75f, 0f, -140.5f, -28.5f)
				reflectiveQuadToRelative(-114f, -77f)
				reflectiveQuadToRelative(-77f, -114f)
				reflectiveQuadTo(120f, 520f)
				reflectiveQuadToRelative(28.5f, -140.5f)
				reflectiveQuadToRelative(77f, -114f)
				reflectiveQuadToRelative(114f, -77f)
				reflectiveQuadTo(480f, 160f)
				reflectiveQuadToRelative(140.5f, 28.5f)
				reflectiveQuadToRelative(114f, 77f)
				reflectiveQuadToRelative(77f, 114f)
				reflectiveQuadTo(840f, 520f)
				reflectiveQuadToRelative(-28.5f, 140.5f)
				reflectiveQuadToRelative(-77f, 114f)
				reflectiveQuadToRelative(-114f, 77f)
				reflectiveQuadTo(480f, 880f)
				moveToRelative(112f, -192f)
				lineToRelative(56f, -56f)
				lineToRelative(-128f, -128f)
				verticalLineToRelative(-184f)
				horizontalLineToRelative(-80f)
				verticalLineToRelative(216f)
				close()
				moveTo(224f, 94f)
				lineToRelative(56f, 56f)
				lineToRelative(-170f, 170f)
				lineToRelative(-56f, -56f)
				close()
				moveToRelative(512f, 0f)
				lineToRelative(170f, 170f)
				lineToRelative(-56f, 56f)
				lineToRelative(-170f, -170f)
				close()
				moveTo(480f, 800f)
				quadToRelative(117f, 0f, 198.5f, -81.5f)
				reflectiveQuadTo(760f, 520f)
				reflectiveQuadToRelative(-81.5f, -198.5f)
				reflectiveQuadTo(480f, 240f)
				reflectiveQuadToRelative(-198.5f, 81.5f)
				reflectiveQuadTo(200f, 520f)
				reflectiveQuadToRelative(81.5f, 198.5f)
				reflectiveQuadTo(480f, 800f)
			}
		}.build()
		return _Alarm!!
	}

private var _Alarm: ImageVector? = null
