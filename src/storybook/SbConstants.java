/*
Storybook: Open Source software for novelists and authors.
Copyright (C) 2008 - 2012 Martin Mustun

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package storybook;

import java.awt.Dimension;
import java.util.Locale;

import storybook.toolkit.I18N;

import net.infonode.docking.View;

/**
 * @author martin
 *
 */
public class SbConstants {

	public final static String DEFAULT_LANG = "en_US";
	public final static String DEFAULT_FONT_NAME = "Dialog";
	public final static int DEFAULT_FONT_SIZE = 12;
	public final static int DEFAULT_FONT_STYLE = 0;
	public final static int DEFAULT_CHRONO_ZOOM = 40;
	public final static int MIN_CHRONO_ZOOM = 20;
	public final static int MAX_CHRONO_ZOOM = 100;
	public final static boolean DEFAULT_CHRONO_LAYOUT_DIRECTION = true;
	public final static boolean DEFAULT_CHRONO_SHOW_DATE_DIFFERENCE = false;
	public final static int DEFAULT_BOOK_ZOOM = 40;
	public final static int MIN_BOOK_ZOOM = 10;
	public final static int MAX_BOOK_ZOOM = 200;
	public final static int DEFAULT_BOOK_HEIGHT_FACTOR = 10;
	public final static int DEFAULT_MANAGE_ZOOM = 10;
	public final static int MIN_MANAGE_ZOOM = 1;
	public final static int MAX_MANAGE_ZOOM = 30;
	public final static int DEFAULT_MANAGE_COLUMNS = 5;
	public final static int DEFAULT_READING_ZOOM = 60;
	public final static int DEFAULT_READING_FONT_SIZE = 11;
	public final static boolean DEFAULT_LEAVE_EDITOR_OPEN = false;
	public final static boolean DEFAULT_USE_HTML_SCENES = true;
	public final static boolean DEFAULT_USE_HTML_DESCR = true;
	public final static boolean DEFAULT_MEMORIA_BALLOON = true;
	public final static boolean DEFAULT_EXPORT_CHAPTER_NUMBERS = true;
	public final static boolean DEFAULT_EXPORT_ROMAN_NUMERALS = false;
	public final static boolean DEFAULT_EXPORT_CHAPTER_TITLES = false;
	public final static boolean DEFAULT_EXPORT_CHAPTER_DATES_LOCATIONS = false;
	public final static boolean DEFAULT_EXPORT_SCENE_TITLES = false;
	public final static boolean DEFAULT_EXPORT_PART_TITLES = false;
	public final static int DEFAULT_SIZE_WIDTH = 1000;
	public final static int DEFAULT_SIZE_HEIGHT = 700;
	public final static int DEFAULT_POS_X = 100;
	public final static int DEFAULT_POS_Y = 100;
	public final static boolean DEFAULT_EDITOR_FULL_TOOLBAR = false;
	public final static String DEFAULT_GOOGLE_MAPS_URL = "http://maps.google.com";

	public enum Storybook {
		PRODUCT_NAME("Storybook"),
		PRODUCT_VERSION("4.0.0"),
		PRODUCT_FULL_NAME(PRODUCT_NAME + " " + PRODUCT_VERSION),
		PRODUCT_RELEASE_DATE("2012-06-12"),
		/* SB5 suppress pro
		PRODUCT_PRO_NAME("Storybook Pro"),
		PRODUCT_PRO_FULL_NAME(PRODUCT_PRO_NAME + " " + PRODUCT_VERSION),
		PRODUCT_PRO_RELEASE_DATE("2012-06-12"),
		*/
		COPYRIGHT_YEAR("2012"),
		COPYRIGHT_COMPANY("Intertec - The Storybook Team"),
		PREFERENCE_DB_NAME("preference"),
		DB_VERSION("4.0"),
		DB_FILE_EXT(".h2.db"),
		DB_CONFIG_EXT(".cfg.xml"),
		IS_PRO_VERSION("true"),
		IS_BETA_VERSION("false");
		final private String text;
		private Storybook(String text) { this.text = text; }
		public String toString() { return text; }
	}

	public enum URL {
		HOMEPAGE_EN("http://www.novelist.ch"),
		HOMEPAGE_DE("http://www.novelist.ch/joomla/index.php/de"),
		/* SB5 suppress Pro
		GO_PRO_EN("http://www.novelist.ch/joomla/index.php/en/go-pro"),
		GO_PRO_DE("http://www.novelist.ch/joomla/index.php/de/go-pro"),
		PRO_ONLY_EN("http://www.novelist.ch/joomla/index.php/en/go-pro"),
		*/
		DOC_EN("http://www.novelist.ch/joomla/index.php/en/documentations"),
		DOC_DE("http://www.novelist.ch/joomla/index.php/de/dokumentation"),
		FAQ_EN("http://www.novelist.ch/joomla/index.php/en/faq"),
		FAQ_DE("http://www.novelist.ch/joomla/index.php/de/faq"),
		REPORT_BUG("http://www.novelist.ch/reportbug"),
		FACEBOOK("http://www.facebook.com/StorybookSoftware"),
		TEASER_URL("http://www.novelist.ch/gopro/4_0/teaser"),
		BANNER_URL("http://www.novelist.ch/gopro/4_0/banner"),
		PW_FORGOTTEN("http://shop.novelist.ch/zencart/index.php?main_page=password_forgotten");
		final private String text;
		private URL(String text) { this.text = text; }
		public String toString() { return text; }
	}

	public enum Directory {
		/* SB5 dictionaries are now at root app
		DICTONARIES("resources/dicts/"),
		*/
		DICTONARIES("dicts/"),
		USER_DICTS("dicts");
		final private String text;
		private Directory(String text) { this.text = text; }
		public String toString() { return text; }
	}

	public static enum Language {
		/** English */
		en_US,
		/** German */
		de_DE,
		/** Spanish */
		es_ES,
		/** Danish */
		da_DK,
		/** Brazilian Portuguese */
		pt_BR,
		/** Italian */
		it_IT,
		/** French */
		fr_FR,
		/** Dutch */
		nl_NL,
		/** Hebrew */
		iw_IL,
		/** Finnish */
		fi_FI,
		/** Swedish */
		sv_SE,
		/** Greek */
		el_GR,
		/** Japanese */
		ja_JP,
		/** Traditional Chinese (Hong Kong) */
		zh_HK,
		/** Russian */
		ru_RU,
		/** Czech */
		cs_CZ,
		/** Simplified Chinese */
		zh_CN,
		/** Polish */
		pl_PL;
		public String getI18N() {
			return I18N.getMsg("msg.common.language." + name());
		}
		public Locale getLocale() {
			Locale locale;
			switch (this) {
			case en_US: locale = Locale.US; break;
			case de_DE: locale = Locale.GERMANY; break;
			case es_ES: locale = new Locale("es", "ES"); break;
			case da_DK: locale = new Locale("da", "DK"); break;
			case pt_BR: locale = new Locale("pt", "BR"); break;
			case it_IT: locale = new Locale("it", "IT"); break;
			case fr_FR: locale = new Locale("fr", "FR"); break;
			case nl_NL: locale = new Locale("nl", "NL"); break;
			case iw_IL: locale = new Locale("iw", "IL"); break;
			case fi_FI: locale = new Locale("fi", "FI"); break;
			case sv_SE: locale = new Locale("sv", "SE"); break;
			case el_GR: locale = new Locale("el", "GR"); break;
			case ja_JP: locale = new Locale("ja", "JP"); break;
			case zh_HK: locale = new Locale("zh", "HK"); break;
			case ru_RU: locale = new Locale("ru", "RU"); break;
			case cs_CZ: locale = new Locale("cs", "CZ"); break;
			case zh_CN: locale = new Locale("zh", "CN"); break;
			case pl_PL: locale = new Locale("pl", "PL"); break;
			default: locale = Locale.US;
			}
			return locale;
		}
	};

	public static enum Spelling {
		none, en_US, de_DE, es_ES, it_IT, fr_FR, ru_RU, nl_NL, pl_PL;
		public String getI18N() {
			if (this == none) {
				return I18N.getMsg("msg.pref.spelling.no");
			}
			return I18N.getMsg("msg.common.language." + name());
		}
	}

	public static enum LookAndFeel {
		cross,
		/*system*/;
		public String getI18N() {
			return I18N.getMsg("msg.pref.laf." + name());
		}
	}

	public enum ViewName {
		SCENES("Scenes"),
		CHAPTERS("Chapters"),
		PARTS("Parts"),
		LOCATIONS("Locations"),
		PERSONS("Persons"),
		GENDERS("Genders"),
		CATEGORIES("Categories"),
		STRANDS("Strands"),
		IDEAS("Ideas"),
		TAGS("Tags"),
		TAGLINKS("TagLinks"),
		ITEMS("Items"),
		ITEMLINKS("ItemLinks"),
		INTERNALS("Internals"),
		EDITOR("Editor"),
		CHRONO("Chrono"),
		BOOK("Book"),
		MANAGE("Manage"),
		READING("Reading"),
		MEMORIA("Memoria"),
		NAVIGATION("Navigation"),
		CHART_PERSONS_BY_DATE("ChartPersonsByDate"),
		CHART_PERSONS_BY_SCENE("ChartPersonsByScene"),
		CHART_WiWW("ChartWiWW"),
		CHART_STRANDS_BY_DATE("ChartStrandsByDate"),
		CHART_OCCURRENCE_OF_PERSONS("ChartOccurrenceOfPersons"),
		CHART_OCCURRENCE_OF_LOCATIONS("ChartOccurrenceOfLocations"),
		CHART_GANTT("ChartGantt"),
		TREE("Tree"),
		INFO("Info");
		final private String text;
		private ViewName(String text) { this.text = text; }
		public String toString() { return text; }
		public boolean compare(View view){ return text.equals(view.getName()); }
	}

	public enum ComponentState {
		ENABLED("Enabled"),
		DISABLED("Disabled");
		final private String text;
		private ComponentState(String text) { this.text = text; }
		public String toString() { return text; }
	}

	public enum ComponentName {
		BT_EDIT("BtEdit"),
		BT_NEW("BtNew"),
		BT_COPY("BtCopy"),
		BT_DELETE("BtDelete"),
		BT_ADD_OR_UPDATE("BtUpdate"),
		BT_OK("BtOk"),
		BT_CANCEL("BtCancel"),
		BT_CLOSE("BtClose"),
		BT_ORDER_UP("BtOrderUp"),
		BT_ORDER_DOWN("BtOrderDown"),
		BT_PRINT("BtPrint"),
		BT_NEXT("BtNext"),
		BT_PREVIOUS("BtPrevious"),
		BT_FIRST("BtFirst"),
		BT_LAST("BtLast"),
		TB_MAIN("MainToolbar"),
		CB_CATEGORY("CbCategory"),
		CB_PERSON("CbPerson"),
		COMBO_ENTITY_TYPES("ComboEntityType"),
		COMBO_ENTITIES("ComboEntities"),
		COMBO_DATES("ComboDates"),
		COMBO_SCENE_STATES("ComboSceneStates");
		final private String text;
		private ComponentName(String text) { this.text = text; }
		public String toString() { return text; }
		public boolean check(String prop) { return text.equals(prop); }
	}

	public enum ActionKey {
		CATEGORY("Category");
		final private String text;
		private ActionKey(String text) { this.text = text; }
		public String toString() { return text; }
	}

	public enum ActionCommand {
		EDIT("Edit"),
		NEW("New"),
		COPY("Copy"),
		DELETE("Delete");
		final private String text;
		private ActionCommand(String text) { this.text = text; }
		public String toString() { return text; }
		public boolean check(String prop) { return text.equals(prop); }
	}

	public enum ClientPropertyName {
		DOCUMENT_MODEL("DocumentModel"),
		DAO_CLASS("DAOClass"),
		ENTITY("Entity"),
		DAO("DAO"),
		IS_NEW_ENTITY("IsNewEntity"),
		COMPONENT_TITLE("ComponentTitle"),
		MAIN_FRAME("MainFrame");
		final private String text;
		private ClientPropertyName(String text) { this.text = text; }
		public String toString() { return text; }
		public boolean check(String prop) { return text.equals(prop); }
	}

	public enum PreferenceKey {
		STORYBOOK_VERSION("StorybookVersion"),
		LAST_OPEN_DIR("LastOpenDir"),
		LAST_OPEN_FILE("LastOpenFile"),
		OPEN_LAST_FILE("OpenLastFile"),
		RECENT_FILES("RecentFiles"),
		CONFIRM_EXIT("ConfirmExit"),
		GOOGLE_MAPS_URL("GoogleMapsURL"),
		SIZE_WIDTH("SizeWidth"),
		SIZE_HEIGHT("SizeHeight"),
		POS_X("PosX"),
		POS_Y("PosY"),
		MAXIMIZED("Maximized"),
		LANG("language"),
		SPELLING("Spelling"),
		LAF("LookAndFeel"),
		FIRST_START_4("FirstStart4"),
		DOCKING_LAYOUT("DockingLayout"),
		DEFAULT_FONT_NAME("DefaultFontName"),
		DEFAULT_FONT_SIZE("DefaultFontSize"),
		DEFAULT_FONT_STYLE("DefaultFontStyle"),
		TRANSLATOR_MODE("TranslatorMode"),
		EMAIL("Email"),
		PASSWORD("Password");
		final private String text;
		private PreferenceKey(String text) { this.text = text; }
		public String toString() { return text; }
	}

	public enum InternalKey {
		CHRONO_ZOOM("ChronoZoom"),
		CHRONO_LAYOUT_DIRECTION("ChornoLayoutDirection"),
		CHRONO_SHOW_DATE_DIFFERENCE("ChornoShowDateDiff"),
		BOOK_ZOOM("BookZoom"),
		BOOK_HEIGHT_FACTOR("BookHeightFactor"),
		MANAGE_ZOOM("ManageZoom"),
		MANAGE_COLUMNS("ManageColumns"),
		READING_ZOOM("ReadingZoom"),
		READING_FONT_SIZE("ReadingFontSize"),
		LEAVE_EDITOR_OPEN("LeaveEditorOpen"),
		LAST_USED_PART("LastUsedPart"),
		USE_HTML_SCENES("UseHtmlScenes"),
		USE_HTML_DESCR("UseHtmlDescr"),
		MEMORIA_BALLOON("MemoriaBalloon"),
		EXPORT_CHAPTER_NUMBERS("ExportChapterNumbers"),
		EXPORT_ROMAN_NUMERALS("ExportRomanNumerals"),
		EXPORT_CHAPTER_TITLES("ExportChapterTitles"),
		EXPORT_CHAPTER_DATES_LOCATIONS("ExportChapterDatesLocations"),
		EXPORT_SCENE_TITLES("ExportSeceneTitles"),
		EXPORT_PART_TITLES("ExportPartTitles"),
		EDITOR_FULL_TOOLBAR("EditorFullToolbar"),
		EXPORT_DIRECTORY("ExportDirectory"),
		TITLE("Title"),
		SUBTITLE("SubTitle"),
		AUTHOR("Author"),
		COPYRIGHT("Copyright"),
		BLURB("Blurb"),
		NOTES("Notes"),
		LAST_USED_LAYOUT("_internal_last_used_layout_");
		final private String text;
		private InternalKey(String text) { this.text = text; }
		public String toString() { return text; }
	}

	public enum IconSize {
		SMALL(new Dimension(16, 16)),
		MEDIUM(new Dimension(32, 32)),
		MEDIUM_WIDE(new Dimension(32, 16)),
		LARGE(new Dimension(32, 32));
		final private Dimension dim;
		private IconSize(Dimension text) { this.dim = text; }
		public String toString() { return dim.toString(); }
		public Dimension getDimension() { return dim; }
	}
}