package com.example.mobilefinal.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Music_note: ImageVector
	get() {
		if (_Music_note != null) {
			return _Music_note!!
		}
		_Music_note = ImageVector.Builder(
            name = "Music_note",
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
				moveTo(400f, 840f)
				quadToRelative(-66f, 0f, -113f, -47f)
				reflectiveQuadToRelative(-47f, -113f)
				reflectiveQuadToRelative(47f, -113f)
				reflectiveQuadToRelative(113f, -47f)
				quadToRelative(23f, 0f, 42.5f, 5.5f)
				reflectiveQuadTo(480f, 542f)
				verticalLineToRelative(-422f)
				horizontalLineToRelative(240f)
				verticalLineToRelative(160f)
				horizontalLineTo(560f)
				verticalLineToRelative(400f)
				quadToRelative(0f, 66f, -47f, 113f)
				reflectiveQuadToRelative(-113f, 47f)
			}
		}.build()
		return _Music_note!!
	}

private var _Music_note: ImageVector? = null
