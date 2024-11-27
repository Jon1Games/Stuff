# Stuff (Minecraft Plugin)

Plugin adding general minecraft features.

## Language:

Default: German
Config to edit the Language available.

## Features:

_Optionally you can enable to only use the API_
_Everything can be enabled/disabled in config_
1.Debug command (Disabled by default)

- Some information about blocks and Items
- give event and recive all listener for that event

2. Calculator
3. Broadcast
4. Command executing Commands
5. Fly
6. Gamemodecommand
7. InfoComamnds
8. MSG
9. Ping
10. Playtime
11. Portableinventories
12. Speed
13. Chat channel
14. Use Minimagge(`<green>TEXt</green>`) or Lagasy(`&7Text`) chat formatter (editable in Config, defautl: Minimessage)
15. First Join Text and Title
16. TAB Prefix/Suffix (Luckperms)
17. TAB Sorting (Luckperms Weight)
18.

## API

1. Chatinput capture
2. ItemStack creator
   1. Name
   2. Lore
   3. Glint
   4. When(Left/Right/Both)Clicked action
   5. WhenPlaced action
3. GUI created out of `List<ItemStack>` (Pagenation)

## ITemStack creator usage

### Stuff API: ClickEvent

```java
public class ClassName implements InventoryHolder{

	private static final ClickEvent event = ClassName::eventI;
	private static final LeftClickEvent event = ClassName::eventI;
	private static final ClickEvent event = ClassName::eventI;

	Inventory inventory;

	public ClassName() {

		this.inventory = Bukkit.createInventory(this, (row * 9), Component.Text();
		// row = anzahl der reihen des Inventars (1-6)

		Stuff.INSTANCE.itemBuilderManager.addClickEvent(changeName, "Plugin:EventName");

		telepadGui.setItem(
			1, // Item index
			new ItemBuilder()
				// Weitere Argumente
				.whenClicked("Plugin:EventName")
				.whenLeftClicked("Plugin:EventName")
				.whenRightClicked("Plugin:EventName")
				.build()
		);

	}

	private static void eventI(InventoryClickEvent e) {
		// event Code
	}

	@Override
	public @NotNull Inventory getInventory() {
		return inventory;
	}

}
```

### Stuff API: PlaceEvent

```java
public class ClassName {

	private static final PlaceEvent event = ClassName::eventI;

	public ClassName() {
<<<<<<< HEAD

		Stuff.INSTANCE.itemBuilderManager.addPlaceEvent(changeName, "Plugin:EventName");

=======
		
		Stuff.INSTANCE.itemBuilderManager.addPlaceEvent(event, "Plugin:EventName");
		
>>>>>>> 8b055270b7e5deeb178c03ee901ac13a562bcd24
		Itemstack item = new ItemBuilder()
			// Weitere Argumente
			.whenPlaced("Plugin:EventName")
			.build();

	private static void eventI(BlockPlaceEvent e) {
		// event Code
	}

}
```

<<<<<<< HEAD
### Stuff API: BreakEvent

```java
public class ClassName {

	private static final BreakEvent event = ClassName::eventI;

	public ClassName() {

		Stuff.INSTANCE.itemBuilderManager.addBreakEvent(changeName, "Plugin:EventName");

		Itemstack item = new ItemBuilder()
			// Weitere Argumente
			.whenBroken("Plugin:EventName")
			.build();
=======
### Stuff API: BlockBreakEvent
```java
public class ClassName {
	
	private static final BreakEvent event = ClassName::eventI;
	
	public ClassName() {
		
		Stuff.INSTANCE.itemBuilderManager.BreakEvent(event, "Plugin:EventName");
		
		Itemstack item = new ItemBuilder()
			// Weitere Argumente
			.whenBroken("Plugin:EventName")
			.build()
		);
>>>>>>> 8b055270b7e5deeb178c03ee901ac13a562bcd24

	private static void eventI(BlockBreakEvent e) {
		// event Code
	}

}
```

<<<<<<< HEAD
### Stuff API: BrokenWithItem

```java
public class ClassName {

	private static final BlockBreakWithItemEvent event = ClassName::eventI;

	public ClassName() {

		Stuff.INSTANCE.itemBuilderManager.addItemBreakBlockEvent(changeName, "Plugin:EventName");

		Itemstack item = new ItemBuilder()
			// Weitere Argumente
			.whenBrokenWithItem("Plugin:EventName")
			.build();

	private static void eventI(BlockBreakEvent e) {
		// event Code
	}

}
=======
### Stuff API: GuiPlaceholder
Uses an existing inventory and fills the given slots with gray_stained_glas_panes
```java
int[] = {0,1,2,3,4,5,6,7,8,9,15}
inv = GuiPlaceholder(inv, int).getInventory();
>>>>>>> 8b055270b7e5deeb178c03ee901ac13a562bcd24
```

### ItemEditor: Arguments

_Events can be registered in other classes_

#### Material

** Material has to be setted**

```java
.setMaterial(Material.Item_Material)
```

or the skull can be setted, this overweites the Material

```java
.setSkull(Player_UUID)
```

#### Name

```java
.setName(String)
```

```java
.setName(Component)
```

#### Glint

```java
.setGlint(boolean)
```

#### Lore

```java
List<Component> lore = new ArrayList<>();
lore.add(Component);
.setLore(lore)
```

```java
.addLore(String)
```

```java
.addLore(Component)
```
