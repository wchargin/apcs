package util;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import tools.customizable.AbstractSwingProperty;
import util.DirectoryProperty.DirectoryEditor;

/**
 * A property for selecting an existing directory reference.
 * 
 * @author William Chargin
 * 
 */
public class DirectoryProperty extends
		AbstractSwingProperty<File, DirectoryEditor, JLabel> {

	/**
	 * The editor for a file property.
	 * 
	 * @author William Chargin
	 * 
	 */
	public class DirectoryEditor extends JPanel {

		/**
                 * 
                 */
		private static final long serialVersionUID = 1L;

		/**
		 * The clear icon.
		 */
		private final ImageIcon iconClear = new ImageIcon(
				DirectoryProperty.class
						.getResource("/tools/customizable/rsc/fp_clear.png")); //$NON-NLS-1$

		/**
		 * The "Select File" button.
		 */
		private final JButton btnSelect;

		/**
		 * The clear button.
		 */
		private final JButton btnClear;

		@SuppressWarnings("serial")
		public DirectoryEditor() {
			super(new MigLayout(new LC().insetsAll(Integer.toString(0))));

			setBorder(BorderFactory.createEmptyBorder());

			btnSelect = new JButton(); //$NON-NLS-1$
			btnSelect.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					JFileChooser fc = new JFileChooser(getValue());
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int ret = fc.showOpenDialog(SwingUtilities
							.getWindowAncestor(btnSelect));
					if (ret == JFileChooser.APPROVE_OPTION) {
						setValue(fc.getSelectedFile());
					}
				}
			});
			add(btnSelect, new CC().growX().pushX());

			btnClear = new JButton(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					setValue(null);
				}
			});
			btnClear.setMaximumSize(new Dimension(25, 25));
			btnClear.setIcon(iconClear);
			add(btnClear);
		}

		@Override
		public void setEnabled(boolean enabled) {
			super.setEnabled(enabled);
			btnSelect.setEnabled(enabled);
			btnClear.setEnabled(enabled);
		}

	}

	/**
	 * The maximum number of characters to display for the file name.
	 */
	private static final int MAX_NAME_LENGTH = 20;

	/**
         * 
         */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates the property with a blank name and {@code null} value.
	 */
	public DirectoryProperty() {
		super();
	}

	/**
	 * Creates the given name and value.
	 * 
	 * @param name
	 *            the property name
	 * @param value
	 *            the value
	 */
	public DirectoryProperty(String name, File value) {
		super(name, value);
	}

	@Override
	protected DirectoryEditor createEditor() {
		return new DirectoryEditor();
	}

	@Override
	protected JLabel createViewer() {
		return new JLabel();
	}

	@Override
	protected void updateEditor(DirectoryEditor editor) {
		editor.btnSelect
				.setText("Select directory..."
						+ (getValue() == null ? ""
								: (" ("
										+ (getValue().getName().length() > MAX_NAME_LENGTH ? getValue()
												.getName()
												.substring(
														0,
														MAX_NAME_LENGTH
																- "...".length())
												+ "..."
												: getValue().getName()) + ")")));
		editor.setEnabled(isEnabled());
	}

	@Override
	protected void updateViewer(JLabel viewer) {
		viewer.setText(getValue() == null ? "No directory selected"
				: getValue().getPath());
	}
}