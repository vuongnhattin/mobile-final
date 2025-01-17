package com.example.mobilefinal.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val PauseCircle: ImageVector
	get() {
		if (_PauseCircle != null) {
			return _PauseCircle!!
		}
		_PauseCircle = ImageVector.Builder(
            name = "PauseCircle",
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
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(8f, 15f)
				arcTo(7f, 7f, 0f, isMoreThanHalf = true, isPositiveArc = true, 8f, 1f)
				arcToRelative(7f, 7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 14f)
				moveToRelative(0f, 1f)
				arcTo(8f, 8f, 0f, isMoreThanHalf = true, isPositiveArc = false, 8f, 0f)
				arcToRelative(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0f, 16f)
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
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(5f, 6.25f)
				arcToRelative(1.25f, 1.25f, 0f, isMoreThanHalf = true, isPositiveArc = true, 2.5f, 0f)
				verticalLineToRelative(3.5f)
				arcToRelative(1.25f, 1.25f, 0f, isMoreThanHalf = true, isPositiveArc = true, -2.5f, 0f)
				close()
				moveToRelative(3.5f, 0f)
				arcToRelative(1.25f, 1.25f, 0f, isMoreThanHalf = true, isPositiveArc = true, 2.5f, 0f)
				verticalLineToRelative(3.5f)
				arcToRelative(1.25f, 1.25f, 0f, isMoreThanHalf = true, isPositiveArc = true, -2.5f, 0f)
				close()
			}
		}.build()
		return _PauseCircle!!
	}

private var _PauseCircle: ImageVector? = null
