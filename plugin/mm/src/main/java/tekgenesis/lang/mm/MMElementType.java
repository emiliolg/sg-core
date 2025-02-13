
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import java.util.EnumMap;
import java.util.stream.Stream;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;

import org.jetbrains.annotations.NotNull;

import tekgenesis.intellij.CommonElementType;
import tekgenesis.mmcompiler.ast.MMToken;

/**
 * The MM ElementType for Intellij.
 */
public class MMElementType extends CommonElementType<MMToken> {

    //~ Constructors .................................................................................................................................

    private MMElementType(@NotNull MMToken tokenType) {
        super(tokenType, MMLanguage.INSTANCE);
    }

    //~ Methods ......................................................................................................................................

    /** Returns an MMElementType corresponding to the specified MMToken. */
    public static MMElementType forToken(@NotNull MMToken tokenType) {
        return map.get(tokenType);
    }

    /** Returns an array of MMElementType corresponding to the specified MMTokens. */
    public static TokenSet forTokens(@NotNull MMToken... tokens) {
        return TokenSet.create((IElementType[]) Stream.of(tokens).map(MMElementType::forToken).toArray(MMElementType[]::new));
    }

    /** Returns an MMToken based on an ASTNode. */
    @NotNull public static MMToken typeOf(@NotNull ASTNode node) {
        return typeOf(node.getElementType());
    }
    @NotNull private static MMToken typeOf(@NotNull IElementType elementType) {
        if (elementType == FILE) return MMToken.FILE;
        if (elementType instanceof MMElementType) return ((MMElementType) elementType).getTokenType();
        throw new IllegalArgumentException("Not an Entity element type: " + elementType);
    }

    //~ Static Fields ................................................................................................................................

    private static final EnumMap<MMToken, MMElementType> map;

    static {
        map = new EnumMap<>(MMToken.class);
        for (final MMToken token : MMToken.values())
            map.put(token, token.isEmpty() ? null : new MMElementType(token));
    }

    public static final MMElementType IDENTIFIER           = forToken(MMToken.IDENTIFIER);
    public static final MMElementType LABELED_ID           = forToken(MMToken.LABELED_ID);
    public static final MMElementType PRIMARY_KEY          = forToken(MMToken.PRIMARY_KEY);
    public static final MMElementType PERMISSIONS          = forToken(MMToken.PERMISSIONS);
    public static final MMElementType LIST                 = forToken(MMToken.LIST);
    public static final MMElementType WITH                 = forToken(MMToken.WITH);
    public static final MMElementType COMMENT              = forToken(MMToken.COMMENT);
    public static final MMElementType LINE_COMMENT         = forToken(MMToken.LINE_COMMENT);
    public static final MMElementType DOCUMENTATION        = forToken(MMToken.DOCUMENTATION);
    public static final MMElementType ENTITY_FIELD         = forToken(MMToken.FIELD);
    public static final MMElementType FIELD_REF            = forToken(MMToken.FIELD_REF);
    public static final MMElementType METHOD_REF           = forToken(MMToken.METHOD_REF);
    public static final MMElementType PERMISSION_REF       = forToken(MMToken.PERMISSION_REF);
    public static final MMElementType ENUM_VALUE           = forToken(MMToken.ENUM_VALUE);
    public static final MMElementType ENUM_FIELD           = forToken(MMToken.ENUM_FIELD);
    public static final MMElementType ENTITY               = forToken(MMToken.ENTITY);
    public static final MMElementType LISTING              = forToken(MMToken.LISTING);
    public static final MMElementType TYPE                 = forToken(MMToken.TYPE_NODE);
    public static final MMElementType TYPE_REF             = forToken(MMToken.TYPE_REF);
    public static final MMElementType COLON                = forToken(MMToken.COLON);
    public static final TokenSet      COMMENTS             = TokenSet.create(COMMENT, LINE_COMMENT);
    public static final MMElementType FORM                 = forToken(MMToken.FORM);
    public static final MMElementType WIDGET_DEF           = forToken(MMToken.WIDGET);
    public static final MMElementType WIDGET               = forToken(MMToken.WIDGET_FIELD);
    public static final MMElementType MENU                 = forToken(MMToken.MENU);
    public static final MMElementType LINK                 = forToken(MMToken.LINK);
    public static final MMElementType ENUM                 = forToken(MMToken.ENUM);
    public static final MMElementType CASE                 = forToken(MMToken.CASE);
    public static final MMElementType TASK                 = forToken(MMToken.TASK);
    public static final MMElementType ROUTE                = forToken(MMToken.ROUTE);
    public static final MMElementType HANDLER              = forToken(MMToken.HANDLER);
    public static final MMElementType MENU_ELEMENT         = forToken(MMToken.MENU_ELEMENT);
    public static final MMElementType ROLE_ELEMENT         = forToken(MMToken.ROLE_ELEMENT);
    public static final MMElementType ROLE_PERMISSION      = forToken(MMToken.ROLE_PERMISSION);
    public static final MMElementType VIEW                 = forToken(MMToken.VIEW);
    public static final MMElementType ROOT                 = forToken(MMToken.FILE);
    public static final MMElementType IGNORABLE            = forToken(MMToken.IGNORABLE);
    public static final MMElementType ENTITY_REF           = forToken(MMToken.ENTITY_REF);
    public static final MMElementType DATA_OBJECT_REF      = forToken(MMToken.DATAOBJECT_REF);
    public static final MMElementType OPTION               = forToken(MMToken.OPTION);
    public static final MMElementType WIDGET_TYPE          = forToken(MMToken.WIDGET_TYPE);
    public static final MMElementType FORM_REF             = forToken(MMToken.FORM_REF);
    public static final MMElementType WIDGET_DEF_REF       = forToken(MMToken.WIDGET_DEF_REF);
    public static final MMElementType REFERENCE            = forToken(MMToken.REFERENCE);
    public static final MMElementType PATH                 = forToken(MMToken.PATH);
    public static final MMElementType INTERPOLATION        = forToken(MMToken.INTERPOLATION);
    public static final MMElementType PACKAGE              = forToken(MMToken.PACKAGE);
    public static final MMElementType SCHEMA               = forToken(MMToken.SCHEMA);
    public static final MMElementType MODEL_OPTIONS        = forToken(MMToken.MODEL_OPTIONS);
    public static final MMElementType ICON                 = forToken(MMToken.ICON);
    public static final MMElementType HTTP_METHOD          = forToken(MMToken.HTTP_METHOD);
    public static final MMElementType CHECK_TYPE           = forToken(MMToken.CHECK_TYPE);
    public static final MMElementType MASK                 = forToken(MMToken.MASK);
    public static final MMElementType FILE_TYPE            = forToken(MMToken.FILE_TYPE);
    public static final MMElementType BUTTON_TYPE          = forToken(MMToken.BUTTON_TYPE);
    public static final MMElementType DATE_TYPE            = forToken(MMToken.DATE_TYPE);
    public static final MMElementType TAB_TYPE             = forToken(MMToken.TAB_TYPE);
    public static final MMElementType POPOVER_TYPE         = forToken(MMToken.POPOVER_TYPE);
    public static final MMElementType MAP_TYPE             = forToken(MMToken.MAP_TYPE);
    public static final MMElementType EXPORT_TYPE          = forToken(MMToken.EXPORT_TYPE);
    public static final MMElementType TOGGLE_BUTTON_TYPE   = forToken(MMToken.TOGGLE_BUTTON_TYPE);
    public static final MMElementType QUERY_MODE           = forToken(MMToken.QUERY_MODE);
    public static final MMElementType SEARCHABLE           = forToken(MMToken.SEARCHABLE);
    public static final MMElementType AFTER_SEARCHABLE     = forToken(MMToken.AFTER_SEARCHABLE);
    public static final MMElementType TASK_TYPE            = forToken(MMToken.TASK_TYPE);
    public static final MMElementType TRANSACTION          = forToken(MMToken.TRANSACTION);
    public static final MMElementType MAIL_VALIDATION_TYPE = forToken(MMToken.MAIL_VALIDATION_TYPE);
    public static final MMElementType RATING_TYPE          = forToken(MMToken.RATING_TYPE);
    public static final MMElementType MODEL                = forToken(MMToken.MODEL);

    private static final MMElementType WHITE_SPACE  = forToken(MMToken.WHITE_SPACE);
    public static final TokenSet       WHITE_SPACES = TokenSet.create(WHITE_SPACE);

    public static final TokenSet SEARCHABLE_OPTS   = TokenSet.create(forToken(MMToken.BY));
    public static final TokenSet TRANSACTION_TYPES = TokenSet.create(forToken(MMToken.NONE),
            forToken(MMToken.ALL),
            forToken(MMToken.ISOLATED),
            forToken(MMToken.EACH));

    public static final TokenSet ENTITY_OPTIONS  = forTokens(MMToken.PRIMARY_KEY,
            MMToken.DESCRIBED_BY,
            MMToken.CACHE,
            MMToken.AUDITABLE,
            MMToken.REMOTABLE,
            MMToken.OPTIMISTIC,
            MMToken.DEPRECABLE,
            MMToken.IMAGE,
            MMToken.INDEX,
            MMToken.SEARCHABLE,
            MMToken.TABLE,
            MMToken.UNIQUE,
            MMToken.FORM);
    public static final TokenSet FORM_OPTIONS    = forTokens(MMToken.ENTITY,
            MMToken.ENUM,
            MMToken.ON_LOAD,
            MMToken.ON_DISPLAY,
            MMToken.ON_CANCEL,
            MMToken.ON_SCHEDULE,
            MMToken.ON_ROUTE,
            MMToken.PRIMARY_KEY,
            MMToken.PERMISSIONS,
            MMToken.LISTING,
            MMToken.HANDLER,
            MMToken.UNRESTRICTED,
            MMToken.PARAMETERS);
    public static final TokenSet HANDLER_OPTIONS = forTokens(MMToken.ON_ROUTE, MMToken.UNRESTRICTED, MMToken.SECURE_BY, MMToken.RAISE);
    public static final TokenSet ENUM_OPTIONS    = forTokens(MMToken.FORM,
            MMToken.WITH,
            MMToken.PRIMARY_KEY,
            MMToken.DEFAULT,
            MMToken.INDEX,
            MMToken.IMPLEMENTS);
    public static final TokenSet CASE_OPTIONS    = forTokens(MMToken.ENTITY, MMToken.FORM, MMToken.WITH);
    public static final TokenSet VIEW_OPTIONS    = forTokens(MMToken.ENTITY,
            MMToken.UPDATABLE,
            MMToken.DESCRIBED_BY,
            MMToken.SEARCHABLE,
            MMToken.UNIQUE,
            MMToken.INDEX,
            MMToken.IMAGE,
            MMToken.BATCH_SIZE);
    public static final TokenSet TASK_OPTIONS    = forTokens(MMToken.SCHEDULE,
            MMToken.TRANSACTION,
            MMToken.EXCLUSION_GROUP,
            MMToken.PATTERN,
            MMToken.NODE,
            MMToken.CLUSTER);
    public static final TokenSet MODELS          = forTokens(MMToken.ENTITY,
            MMToken.ENUM,
            MMToken.MENU,
            MMToken.CASE,
            MMToken.FORM,
            MMToken.WIDGET,
            MMToken.HANDLER,
            MMToken.LINK,
            MMToken.TASK,
            MMToken.VIEW);

    public static final MMElementType STRING_LITERAL = forToken(MMToken.STRING_LITERAL);
    public static final TokenSet      LITERALS       = TokenSet.create(STRING_LITERAL);

    public static final IFileElementType FILE = new IFileElementType(MMLanguage.INSTANCE);

    public static final TokenSet      BRACES = TokenSet.create(forToken(MMToken.LEFT_BRACE), forToken(MMToken.RIGHT_BRACE));
    public static final MMElementType EMPTY  = new MMElementType(MMToken.EMPTY_TOKEN);
}  // end class MMElementType
