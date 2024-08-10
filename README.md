# Stuff (Minecraft Plugin)
Plugin adding general minecraft features.

## Language:
Default: German
Config to edit the Language available.

## Features:
*Everything can be enabled/disabled in config*
1. Calculator
2. Broadcast
3. Command executing Commands
4. Fly
5. Gamemodecommand
6. InfoComamnds
7. MSG
8. Ping
9. Playtime
10. Portableinventories
11. Speed
12. Chat channel
13. Use Minimagge(`<green>TEXt</green>`) or Lagasy(`&7Text`) chat formatter (editable in Config, defautl: Minimessage)
14. First Join Text and Title
15. TAB Prefix/Suffix (Luckperms)
16. TAB Sorting (Luckperms Weight)

## API
1. Chatinput capture
2. ItemStack creator (mainly for GUI) *Usage Later*
   1. Name
   2. Lore
   3. Glint
   4. When Clicked action 
   5. When Placed action
4. GUI created out of `List<ItemStack>`

## ITemStack creator usage
### Stuff API: ClickEvent
```java
public class ClassName implements InventoryHolder{

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
### Arguments
*Events can be registered in other classes*
#### Material
** Material has to be setted**
```java
.setMaterial(Material.Item_Material)
```
#### Name
```java
.setName(String)
```
```java
.setName(Component)
```
#### Clint
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
