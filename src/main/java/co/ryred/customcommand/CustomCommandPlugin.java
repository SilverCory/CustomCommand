package co.ryred.customcommand;

import co.ryred.uuidcredits.BukkitListener;
import co.ryred.uuidcredits.Credits;
import com.comphenix.packetwrapper.WrapperPlayClientChat;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author Cory Redmond
 *         Created by acech_000 on 14/10/2015.
 */
public class CustomCommandPlugin extends JavaPlugin
{

	public static char newCommandChar;

	@Override
	public void onEnable()
	{

		try {
			Credits.initBukkit( new Credits() {
				@Override
				public void startBukkit( BukkitListener listener, Runnable getter )
				{
					getServer().getScheduler().runTaskAsynchronously( CustomCommandPlugin.this, getter );
					getServer().getPluginManager().registerEvents( listener, CustomCommandPlugin.this );
				}
			} );
		} catch ( Exception e ) {}

		if( !(new File( getDataFolder(), "config.yml" )).exists() )
			saveDefaultConfig();

		newCommandChar = getConfig().getString( "command-char", "/" ).charAt( 0 );

		ProtocolLibrary.getProtocolManager().addPacketListener( new PacketAdapter( this, PacketType.Play.Client.CHAT ) {

			@Override
			public void onPacketReceiving( PacketEvent event )
			{
			
				WrapperPlayClientChat chatPacket = new WrapperPlayClientChat( event.getPacket() );
				if(chatPacket.getMessage().charAt( 0 ) == newCommandChar) {
					chatPacket.setMessage( "/" + chatPacket.getMessage().substring( 1 ) );
				}

				event.setPacket( chatPacket.getHandle() );

			}
			
		} );

	}
}
