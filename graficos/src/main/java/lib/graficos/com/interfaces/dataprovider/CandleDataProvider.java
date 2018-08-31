package lib.graficos.com.interfaces.dataprovider;

import lib.graficos.com.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
