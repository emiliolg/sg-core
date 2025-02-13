
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.formatter;

import java.math.BigDecimal;

import tekgenesis.metadata.form.widget.InputHandlerMetadata;
import tekgenesis.metadata.form.widget.PredefinedMask;
import tekgenesis.type.DecimalType;
import tekgenesis.type.IntType;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.metadata.form.widget.InputHandlerMetadata.Mask;
import static tekgenesis.view.client.formatter.Formatter.*;
import static tekgenesis.view.client.formatter.InputFilter.NONE_FILTER;
import static tekgenesis.view.client.formatter.LocalizedConverter.*;

/**
 * Factory to create an {@link InputHandler}.
 */
public abstract class InputHandlerFactory {

    //~ Constructors .................................................................................................................................

    private InputHandlerFactory() {}

    //~ Methods ......................................................................................................................................

    /** Creates an {@link InputHandler} based on the metadata. */
    public static InputHandler<?> create(final InputHandlerMetadata handlerMetadata, final Type modelType, final boolean signed) {
        final InputHandler<?> result;

        switch (handlerMetadata.getType()) {
        case MASK:
            final Mask             mask             = (Mask) handlerMetadata;
            final MaskInputHandler maskInputHandler = new MaskInputHandler(mask.getMask());
            result = InputHandler.create(STRING_CONVERTER, maskInputHandler, maskInputHandler);
            break;
        case PREDEFINED_MASK:
            final PredefinedMask predefined = ((InputHandlerMetadata.Predefined) handlerMetadata).getMask();
            // final MaskInputHandler m = new MaskInputHandler("A###", "A###");
            // if (predefined == PredefinedMask.IDENTIFIER) result = InputHandler.create(STRING_CONVERTER, m, m);
            result = createPredefinedMaskHandler(modelType, signed, predefined);
            break;
        case INHERIT_FROM_TYPE:
        default:
            result = createInherit(modelType, signed);
            break;
        }

        return result;
    }

    private static InputHandler<Boolean> bool() {
        return InputHandler.create(BOOLEAN_CONVERTER, BOOLEAN_FORMATTER, NONE_FILTER);
    }

    private static InputHandler<?> createInherit(final Type modelType, boolean signed) {
        final InputHandler<?> result;
        switch (modelType.getKind()) {
        case INT:
            final IntType mt = (IntType) modelType;
            result = mt.isLong() ? numericLong(mt, signed) : numericInt(mt, signed);
            break;
        case REAL:
            result = numericReal(signed);
            break;
        case DECIMAL:
            result = numericDecimal((DecimalType) modelType, signed);
            break;
        case DATE_TIME:
            result = dateTime();
            break;
        case DATE:
            result = dateOnly();
            break;
        case BOOLEAN:
            result = bool();
            break;
        case NULL:
            result = InputHandler.none();
            break;
        default:
            result = InputHandler.string();
        }
        return result;
    }

    private static Formatter<Object> createPredefinedFormatter(final Type modelType, final PredefinedMask type) {
        final Formatter<Object> result;
        switch (type) {
        case CURRENCY:
            result = PredefinedFormatters.CURRENCY;
            break;
        case PERCENT:
            result = PredefinedFormatters.PERCENT;
            break;
        case SCIENTIFIC:
            result = PredefinedFormatters.SCIENTIFIC;
            break;
        case DECIMAL:
            result = PredefinedFormatters.DECIMAL;
            break;
        case UNIT:
            result = PredefinedFormatters.UNIT;
            break;
        case RAW:
            result = PredefinedFormatters.RAW;
            break;
        case UPPERCASE:
            result = cast(Formatter.UPPERCASE_FORMATTER);
            break;
        case LOWERCASE:
            result = cast(Formatter.LOWERCASE_FORMATTER);
            break;
        case TIME_AGO:
            if (modelType.getKind() == Kind.DATE_TIME) result = cast(Formatter.TIME_AGO_DATE_TIME_FORMATTER);
            else if (modelType.getKind() == Kind.DATE) result = cast(Formatter.TIME_AGO_DATE_FORMATTER);
            else result = cast(Formatter.STRING_FORMATTER);
            break;
        case FILE_SIZE:
            result = cast(Formatter.FILE_SIZE_FORMATTER);
            break;
        case NONE:
        default:
            result = cast(Formatter.STRING_FORMATTER);
        }
        return result;
    }  // end method createPredefinedFormatter

    @SuppressWarnings("unchecked")
    private static InputHandler<Object> createPredefinedMaskHandler(Type modelType, boolean signed, PredefinedMask mask) {
        final InputHandler<Object> result;
        if (mask == PredefinedMask.UPPERCASE || mask == PredefinedMask.LOWERCASE)
            result = cast(mask == PredefinedMask.UPPERCASE ? InputHandler.uppercase() : InputHandler.lowercase());
        else if (mask == PredefinedMask.IDENTIFIER) result = cast(InputHandler.id());
        else {
            result = (InputHandler<Object>) createInherit(modelType, signed);
            result.setFormatter(createPredefinedFormatter(modelType, mask));
        }
        return result;
    }  // end method createPredefinedMaskHandler

    private static InputHandler<Long> dateOnly() {
        return InputHandler.create(LONG_CONVERTER, DATE_FORMATTER, NONE_FILTER);
    }

    private static InputHandler<Long> dateTime() {
        return InputHandler.create(LONG_CONVERTER, DATE_TIME_FORMATTER, NONE_FILTER);
    }

    private static InputHandler<BigDecimal> numericDecimal(final DecimalType decimalType, boolean signed) {
        return InputHandler.create(Decimal.converter(decimalType.getDecimals()),
            DecimalFormatter.create(decimalType.getDecimals()),
            InputFilter.DecimalInputFilter.create(decimalType, signed));
    }

    private static InputHandler<Integer> numericInt(final IntType intType, boolean signed) {
        return InputHandler.create(INT_CONVERTER, INT_FORMATTER, InputFilter.IntInputFilter.create(intType, signed));
    }

    private static InputHandler<Long> numericLong(final IntType intType, boolean signed) {
        return InputHandler.create(LONG_CONVERTER, LONG_FORMATTER, InputFilter.IntInputFilter.create(intType, signed));
    }

    private static InputHandler<Double> numericReal(boolean signed) {
        return InputHandler.create(REAL_CONVERTER, REAL_FORMATTER, InputFilter.RealInputFilter.create(signed));
    }
}  // end class InputHandlerFactory
