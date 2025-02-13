# Menus

Menus are a way to organize your forms in the UI. If you don't specify any menu a default menu is generated based on all your forms.

@inline-code(doc/samples/doc-project/src/main/mm/tekgenesis/doc/Samples.mm#menu)

### Menu syntax

```
menu <id> <label>
{
	( [<label> : ] <formId> |
	menu <menuId> | 
	<id> <label> : link <url> )+
}
```

## Menu Items

There are three kinds of Menu Items:
- Forms
- SubMenus
- Links

### Forms

Form's menu item is the most common use case. 
@inline-code(doc/samples/doc-project/src/main/mm/tekgenesis/doc/Samples.mm#menu2)

### Menus

Up to three levels of menus is supported. Users can used menus inside menus to create a submenu hierarchy.

```
menu menuA { ... }
menu menuB {
	menuA;
}
menu main {
	menuA;
}
```

### Links

Link menus are the way to point to user applications in external websites.

@inline-code(doc/samples/doc-project/src/main/mm/tekgenesis/doc/Samples.mm#linkMenu)

## Permissions

Permission are handled based on form permissions. When a user does not have permission over a form, this item is not showed in the menu.

@inline-code(doc/samples/doc-project/src/main/mm/tekgenesis/doc/Samples.mm#longMenu)
For instance, if the user has permission only for form1 and form2, the generated menu only will only have these two options. 

If the menus structure has a hierarchy the same logic is applied, and all sub-menus filtered. Also, if any submenu is empty, it will not be shown in the UI.