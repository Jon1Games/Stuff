# Stuff (Minecraft Plugin)
Plugin adding general minecraft features.

## Language:
Default: German
Config to edit the Language available.

## Features:
*Optionally you can enable to only use the API*
*Everything can be enabled/disabled in config*
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
2. ItemStack creator (mainly for GUI) *Usage Later*
   1. Name
   2. Lore
   3. Glint
   4. When(Left/Right/Both)Clicked action 
   5. WhenPlaced action
4. GUI created out of `List<ItemStack>` (Pagenation)

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
		
		Stuff.INSTANCE.itemBuilderManager.addPlaceEvent(changeName, "Plugin:EventName");
		
		Itemstack item = new ItemBuilder()
			// Weitere Argumente
			.whenPlaced("Plugin:EventName")
			.build()
		);

	private static void eventI(BlockPlaceEvent e) {
		// event Code
	}

}
```
### ItemEditor: Arguments
*Events can be registered in other classes*
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
