package util;

import java.awt.Font;

import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

/**
 * A static class for optimizing the GUI look and feel.
 * 
 * @author William Chargin
 * 
 */
public class LAFOptimizer {

        /**
         * Instantiation of this class is not permitted.
         * 
         * @throws IllegalStateException
         *             always
         */
        private LAFOptimizer() throws IllegalStateException {
                throw new IllegalStateException();
        }

        /**
         * Optimizes the look and feel to integrate with the operating system as
         * much as possible.
         * <p>
         * To do this, this method at the installed look and feels and finds the
         * system look and feel. If this is the same as the Metal look and feel, the
         * method disables bold fonts to limit eyesores. If the element is the
         * Windows look and feel, this method fixes a few known limitations
         * contained therein (combo boxes have weird borders; text areas use
         * monospaced fonts).
         */
        public static void optimizeSwing() {
                String systemLaf = UIManager.getSystemLookAndFeelClassName();
                try {
                        if (systemLaf.toLowerCase().contains("metal")) { //$NON-NLS-1$
                                // Seriously? Well, minimize eyesores.
                                UIManager.put("swing.boldMetal", Boolean.FALSE); //$NON-NLS-1$
                        }
                        UIManager.setLookAndFeel(systemLaf);
                } catch (Exception e) {
                        // Last resort.
                        UIManager.put("swing.boldMetal", Boolean.FALSE); //$NON-NLS-1$
                }

                if (systemLaf.toLowerCase().contains("windows")) { //$NON-NLS-1$
                        // Windows LAF uses monospaced fonts for JTextArea.
                        // Let's fix that.
                        Font defaultTextAreaFont = UIManager.getFont("TextArea.font"); //$NON-NLS-1$
                        UIManager.put(
                                        "TextArea.font", //$NON-NLS-1$
                                        new FontUIResource(Font.SANS_SERIF, defaultTextAreaFont
                                                        .getStyle(), defaultTextAreaFont.getSize()));

                        // Again, Windows messes up combo box display with an "XPFillBorder"
                        if (UIManager.getBorder("ComboBox.border").getClass().getName() //$NON-NLS-1$
                                        .contains("XPFillBorder")) { //$NON-NLS-1$
                                UIManager.put("ComboBox.border", new EmptyBorder(0, 0, 0, 0)); //$NON-NLS-1$
                        }
                }
        }

}