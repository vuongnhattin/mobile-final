package com.example.mobilefinal.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val BellSlash: ImageVector
	get() {
		if (_BellSlash != null) {
			return _BellSlash!!
		}
		_BellSlash = ImageVector.Builder(
            name = "BellSlash",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF0F172A)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.5f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(9.14314f, 17.0818f)
				curveTo(10.0799f, 17.1929f, 11.0332f, 17.25f, 11.9998f, 17.25f)
				curveTo(12.3306f, 17.25f, 12.6599f, 17.2433f, 12.9874f, 17.2301f)
				moveTo(9.14314f, 17.0818f)
				curveTo(7.2484f, 16.857f, 5.4214f, 16.4116f, 3.6885f, 15.7719f)
				curveTo(5.0254f, 14.2879f, 5.8755f, 12.3567f, 5.9872f, 10.2299f)
				moveTo(9.14314f, 17.0818f)
				curveTo(9.0502f, 17.3712f, 9f, 17.6797f, 9f, 18f)
				curveTo(9f, 19.6569f, 10.3431f, 21f, 12f, 21f)
				curveTo(13.2864f, 21f, 14.3837f, 20.1903f, 14.8101f, 19.0527f)
				moveTo(16.7749f, 16.7749f)
				lineTo(21f, 21f)
				moveTo(16.7749f, 16.7749f)
				curveTo(17.9894f, 16.5298f, 19.1706f, 16.1929f, 20.3111f, 15.7719f)
				curveTo(18.8743f, 14.177f, 17.9998f, 12.0656f, 17.9998f, 9.75f)
				verticalLineTo(9.04919f)
				lineTo(18f, 9f)
				curveTo(18f, 5.6863f, 15.3137f, 3f, 12f, 3f)
				curveTo(9.5667f, 3f, 7.4717f, 4.4485f, 6.5303f, 6.5303f)
				moveTo(16.7749f, 16.7749f)
				lineTo(6.53026f, 6.53026f)
				moveTo(3f, 3f)
				lineTo(6.53026f, 6.53026f)
			}
		}.build()
		return _BellSlash!!
	}

private var _BellSlash: ImageVector? = null
