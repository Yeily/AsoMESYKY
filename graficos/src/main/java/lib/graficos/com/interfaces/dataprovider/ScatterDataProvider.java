package lib.graficos.com.interfaces.dataprovider;

import lib.graficos.com.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
