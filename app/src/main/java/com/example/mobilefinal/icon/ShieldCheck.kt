package com.example.mobilefinal.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val ShieldCheck: ImageVector
	get() {
		if (_ShieldCheck != null) {
			return _ShieldCheck!!
		}
		_ShieldCheck = ImageVector.Builder(
            name = "ShieldCheck",
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
				moveTo(9f, 12.7498f)
				lineTo(11.25f, 14.9998f)
				lineTo(15f, 9.74985f)
				moveTo(12f, 2.71411f)
				curveTo(9.8495f, 4.7507f, 6.9456f, 5.9999f, 3.75f, 5.9999f)
				curveTo(3.6992f, 5.9999f, 3.6485f, 5.9996f, 3.5979f, 5.9989f)
				curveTo(3.2099f, 7.179f, 3f, 8.4399f, 3f, 9.7499f)
				curveTo(3f, 15.3414f, 6.8243f, 20.0397f, 12f, 21.3719f)
				curveTo(17.1757f, 20.0397f, 21f, 15.3414f, 21f, 9.7499f)
				curveTo(21f, 8.4399f, 20.7901f, 7.179f, 20.4021f, 5.9989f)
				curveTo(20.3515f, 5.9996f, 20.3008f, 5.9999f, 20.25f, 5.9999f)
				curveTo(17.0544f, 5.9999f, 14.1505f, 4.7507f, 12f, 2.7141f)
				close()
			}
		}.build()
		return _ShieldCheck!!
	}

private var _ShieldCheck: ImageVector? = null
