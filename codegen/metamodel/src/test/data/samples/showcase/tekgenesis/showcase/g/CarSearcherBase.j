package tekgenesis.showcase.g;

import tekgenesis.showcase.Car;
import tekgenesis.showcase.CarSearcher;
import tekgenesis.index.SearchableField.Ent;
import tekgenesis.index.IndexSearcher;
import tekgenesis.showcase.Make;
import tekgenesis.showcase.Model;
import org.jetbrains.annotations.NotNull;

/** Base class for index and searching tekgenesis.showcase.Car */
public class CarSearcherBase
    extends IndexSearcher
{

    //~ Fields ...................................................................................................................

    @NotNull public final Ent<Make> MAKE;
    @NotNull public final Ent<Model> MODEL;

    //~ Constructors .............................................................................................................

    /** Default constructor. */
    protected CarSearcherBase() {
        super(Car.class);
        MAKE = fields().entityField("make", "make", Make.class);
        MODEL = fields().entityField("model", "model", Model.class).withBoost(3);
    }

    //~ Methods ..................................................................................................................

    @Override @NotNull public final String getIndexId() { return INDEX_ID; }

    //~ Fields ...................................................................................................................

    @NotNull private static final String INDEX_ID = "e538966bf0aab0e27316f3437f460d";
    @NotNull public static final CarSearcher CAR_SEARCHER = new CarSearcher();

}
