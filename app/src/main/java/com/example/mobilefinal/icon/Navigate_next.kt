package com.example.mobilefinal.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Navigate_next: ImageVector
	get() {
		if (_Navigate_next != null) {
			return _Navigate_next!!
		}
		_Navigate_next = ImageVector.Builder(
            name = "Navigate_next",
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
				moveTo(504f, 480f)
				lineTo(320f, 296f)
				lineToRelative(56f, -56f)
				lineToRelative(240f, 240f)
				lineToRelative(-240f, 240f)
				lineToRelative(-56f, -56f)
				close()
			}
		}.build()
		return _Navigate_next!!
	}

private var _Navigate_next: ImageVector? = null
