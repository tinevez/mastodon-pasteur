package org.mastodon.mamut.io.csv.plugin;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import org.mastodon.app.MastodonIcons;
import org.mastodon.app.ui.ViewMenuBuilder.MenuItem;
import org.mastodon.mamut.MamutMenuBuilder;
import org.mastodon.mamut.io.csv.plugin.ui.CSVImporterUIController;
import org.mastodon.mamut.model.Model;
import org.mastodon.mamut.plugin.MamutPlugin;
import org.mastodon.mamut.plugin.MamutPluginAppModel;
import org.mastodon.ui.keymap.CommandDescriptionProvider;
import org.mastodon.ui.keymap.CommandDescriptions;
import org.mastodon.ui.keymap.KeyConfigContexts;
import org.scijava.plugin.Plugin;
import org.scijava.ui.behaviour.util.AbstractNamedAction;
import org.scijava.ui.behaviour.util.Actions;

@Plugin( type = CSVImporterPlugin.class )
public class CSVImporterPlugin implements MamutPlugin
{

	public static final String[] MENU_PATH = new String[] { "Plugins" };

	public static final String SHOW_CSV_IMPORTER_DIALOG_ACTION = "show csv importer dialog";

	private static final String[] ACTION_1_KEYS = new String[] { "not mapped" };

	private static Map< String, String > menuTexts = new HashMap<>();

	static
	{
		menuTexts.put( SHOW_CSV_IMPORTER_DIALOG_ACTION, "CSV Importer" );
	}

	private final ToggleCSVImporterDialogAction toggleImporterDialog = new ToggleCSVImporterDialogAction();

	@Override
	public Map< String, String > getMenuTexts()
	{
		return menuTexts;
	}

	@Override
	public List< MenuItem > getMenuItems()
	{
		return Arrays.asList( makeFullMenuItem( MamutMenuBuilder.item( SHOW_CSV_IMPORTER_DIALOG_ACTION ) ) );
	}

	@Override
	public void installGlobalActions( final Actions actions )
	{
		actions.namedAction( toggleImporterDialog, ACTION_1_KEYS );
	}

	@Override
	public void setAppPluginModel( final MamutPluginAppModel appModel )
	{
		if ( null == appModel.getAppModel().getModel() )
			return;

		toggleImporterDialog.setModel( appModel.getAppModel().getModel() );
	}

	private static final MenuItem makeFullMenuItem( final MenuItem item )
	{
		MenuItem menuPath = item;
		for ( int i = MENU_PATH.length - 1; i >= 0; i-- )
			menuPath = MamutMenuBuilder.menu( MENU_PATH[ i ], menuPath );
		return menuPath;
	}

	/**
	 * Command descriptions for all provided commands
	 */
	@Plugin( type = Descriptions.class )
	public static class Descriptions extends CommandDescriptionProvider
	{
		public Descriptions()
		{
			super( KeyConfigContexts.MASTODON );
		}

		@Override
		public void getCommandDescriptions( final CommandDescriptions descriptions )
		{
			descriptions.add( SHOW_CSV_IMPORTER_DIALOG_ACTION, ACTION_1_KEYS, "Show the CSV importer dialog." );
		}
	}

	public static class ToggleCSVImporterDialogAction extends AbstractNamedAction
	{

		private static final long serialVersionUID = 1L;

		private JDialog csvImporterDialog;

		private CSVImporterUIController controller;

		public ToggleCSVImporterDialogAction()
		{
			super( SHOW_CSV_IMPORTER_DIALOG_ACTION );
		}

		public void setModel( final Model model )
		{
			controller = new CSVImporterUIController( model );
			csvImporterDialog = new JDialog( ( Frame ) null, "CSV Importer" );
			csvImporterDialog.setIconImages( Arrays.asList( new Image[] {
					MastodonIcons.LOAD_ICON_LARGE.getImage(),
					MastodonIcons.LOAD_ICON_MEDIUM.getImage(),
					MastodonIcons.LOAD_ICON_SMALL.getImage() } ) );
			csvImporterDialog.getContentPane().add( controller.getView(), BorderLayout.CENTER );
			csvImporterDialog.setDefaultCloseOperation( WindowConstants.HIDE_ON_CLOSE );
			csvImporterDialog.setLocationRelativeTo( null );
			csvImporterDialog.pack();
		}

		@Override
		public void actionPerformed( final ActionEvent e )
		{
			if ( null == csvImporterDialog )
				return;
			csvImporterDialog.setVisible( !csvImporterDialog.isVisible() );
		}

		public CSVImporterUIController getController()
		{
			return controller;
		}
	}
}
