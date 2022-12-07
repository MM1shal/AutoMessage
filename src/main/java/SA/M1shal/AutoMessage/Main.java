package SA.M1shal.AutoMessage;

import net.labymod.api.LabyModAddon;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.labymod.utils.ServerData;
import net.minecraft.client.Minecraft;

import java.util.List;

public class Main extends LabyModAddon {

	private boolean Enabled;
	private String text;
	private int After;

	@Override
	public void onEnable() {

		LabyMod.getInstance().getEventManager().registerOnJoin(new Consumer<ServerData>() {
			@Override
			public void accept(ServerData serverData) {
				if(Enabled){
					new java.util.Timer().schedule(
							new java.util.TimerTask() {
								@Override
								public void run() {
									Minecraft.getMinecraft().thePlayer.sendChatMessage(text);
								}
							},
							(After * 1000L)
					);
				}
			}
		});

	}
	@Override
	public void loadConfig() {
		// Loading config value
		this.Enabled = !this.getConfig().has("enabled" ) || this.getConfig().get("enabled").getAsBoolean();
		this.After = getConfig().has( "After" ) ? getConfig().get( "After" ).getAsInt() : 10;
		this.text = getConfig().has("Text") ? getConfig().get("Text").getAsString() : "Hello I`m Using Auto Join/Text Command from LabyMod!";
	}


	/**
	 * Called when the addon's ingame settings should be filled
	 *
	 * @param subSettings a list containing the addon's settings' elements
	 */
	@Override
	protected void fillSettings( List<SettingsElement> subSettings ) {



		subSettings.add( new BooleanElement( "Enabled", new ControlElement.IconData( Material.EMERALD ), new Consumer<Boolean>() {
			@Override
			public void accept( Boolean accepted ) {
				Main.this.Enabled = accepted;
				Main.this.getConfig().addProperty("enabled", accepted);
				Main.this.saveConfig();
			}
		}, this.Enabled ) );

		NumberElement numberElement = new NumberElement( "Send After (seconds)" /* Display name */,
				new ControlElement.IconData( Material.WATCH ) /* setting's icon */, 10 /* current value */ );

// Adding change listener
		numberElement.addCallback( new Consumer<Integer>() {
			@Override
			public void accept( Integer accepted ) {
				Main.this.After = accepted;
				Main.this.getConfig().addProperty("After", accepted);
				Main.this.saveConfig();
			}
		} );

// Adding to settings
		subSettings.add( numberElement );

		subSettings.add( new StringElement( "Text/Command",  new ControlElement.IconData( Material.PAPER ), this.text, new Consumer<String>() {
			@Override
			public void accept( String text ) {

				if(text == null || text.equals("")){
					text = "Hello I`m Using Auto Join/Text Command from LabyMod!";
				}
				Main.this.text = text;
				Main.this.getConfig().addProperty("Text", text);
				Main.this.saveConfig();
			}
		}));

	}
}
