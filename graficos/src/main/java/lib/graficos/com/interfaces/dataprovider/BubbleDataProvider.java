package lib.graficos.com.interfaces.dataprovider;

import lib.graficos.com.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
