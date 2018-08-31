package lib.graficos.com.interfaces.dataprovider;

import lib.graficos.com.components.YAxis.AxisDependency;
import lib.graficos.com.data.BarLineScatterCandleBubbleData;
import lib.graficos.com.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
