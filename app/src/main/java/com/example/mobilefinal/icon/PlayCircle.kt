package com.example.mobilefinal.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val PlayCircle: ImageVector
	get() {
		if (_PlayCircle != null) {
			return _PlayCircle!!
		}
		_PlayCircle = ImageVector.Builder(
            name = "PlayCircle",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
			path(
    			fill = SolidColor(Color(0xFF000000)),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.EvenOdd
			) {
				moveTo(8.6f, 1f)
				curveToRelative(1.6f, 0.1f, 3.1f, 0.9f, 4.2f, 2f)
				curveToRelative(1.3f, 1.4f, 2f, 3.1f, 2f, 5.1f)
				curveToRelative(0f, 1.6f, -0.6f, 3.1f, -1.6f, 4.4f)
				curveToRelative(-1f, 1.2f, -2.4f, 2.1f, -4f, 2.4f)
				curveToRelative(-1.6f, 0.3f, -3.2f, 0.1f, -4.6f, -0.7f)
				curveToRelative(-1.4f, -0.8f, -2.5f, -2f, -3.1f, -3.5f)
				curveTo(0.9f, 9.2f, 0.8f, 7.5f, 1.3f, 6f)
				curveToRelative(0.5f, -1.6f, 1.4f, -2.9f, 2.8f, -3.8f)
				curveTo(5.4f, 1.3f, 7f, 0.9f, 8.6f, 1f)
				close()
				moveToRelative(0.5f, 12.9f)
				curveToRelative(1.3f, -0.3f, 2.5f, -1f, 3.4f, -2.1f)
				curveToRelative(0.8f, -1.1f, 1.3f, -2.4f, 1.2f, -3.8f)
				curveToRelative(0f, -1.6f, -0.6f, -3.2f, -1.7f, -4.3f)
				curveToRelative(-1f, -1f, -2.2f, -1.6f, -3.6f, -1.7f)
				curveToRelative(-1.3f, -0.1f, -2.7f, 0.2f, -3.8f, 1f)
				curveToRelative(-1.1f, 0.8f, -1.9f, 1.9f, -2.3f, 3.3f)
				curveToRelative(-0.4f, 1.3f, -0.4f, 2.7f, 0.2f, 4f)
				curveToRelative(0.6f, 1.3f, 1.5f, 2.3f, 2.7f, 3f)
				curveToRelative(1.2f, 0.7f, 2.6f, 0.9f, 3.9f, 0.6f)
				close()
			}
			path(
    			fill = SolidColor(Color(0xFF000000)),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.EvenOdd
			) {
				moveTo(6f, 5f)
				lineToRelative(0.777f, -0.416f)
				lineToRelative(4.5f, 3f)
				verticalLineToRelative(0.832f)
				lineToRelative(-4.5f, 3f)
				lineTo(6f, 11f)
				verticalLineTo(5f)
				close()
				moveToRelative(1f, 0.934f)
				verticalLineToRelative(4.132f)
				lineTo(10.099f, 8f)
				lineTo(7f, 5.934f)
				close()
			}
		}.build()
		return _PlayCircle!!
	}

private var _PlayCircle: ImageVector? = null
