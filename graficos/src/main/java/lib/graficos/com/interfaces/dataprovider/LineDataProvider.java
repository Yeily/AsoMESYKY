package lib.graficos.com.interfaces.dataprovider;

import lib.graficos.com.components.YAxis;
import lib.graficos.com.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
