/*
Copyright (C) 2007-2011  Database Group - Universita' della Basilicata
Giansalvatore Mecca - giansalvatore.mecca@unibas.it
Salvatore Raunich - salrau@gmail.com
Marcello Buoncristiano - marcello.buoncristiano@yahoo.it

This file is part of ++Spicy - a Schema Mapping and Data Exchange Tool

++Spicy is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
any later version.

++Spicy is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with ++Spicy.  If not, see <http://www.gnu.org/licenses/>.
 */
package gr.aueb.mipmapgui;

import java.awt.Color;

public class Costanti {

    //********************* SERVER FOLDERS/PATHS ***************************
    //public static final String SERVER_MAIN_FOLDER = System.getenv("MIPMAP_HOME").replace("", "");
    public static final String SERVER_MAIN_FOLDER = "/home/ioannisxar/Dropbox/aueb-wim-master/mipmap/WebMIPMap";
    public static final String SERVER_FILES_FOLDER = "/uploaded_files/";
    public static final String SERVER_SCHEMATA_FOLDER = "/schemata/";
    public static final String SERVER_GLOBAL_TASKS_PATH = "/global/admin_users/global_to_all/";
    public static final String SERVER_TEMP_FOLDER = "temp/";
    public static final String SERVER_TEMP_TASKS_FOLDER = "temporary_task_files/";
    public static final String SERVER_PUBLIC_USER_FOLDER = "public/";
    public static final String SERVER_PRIVATE_USER_FOLDER = "priv/";
    public static final String SERVER_SOURCE_FOLDER = "source/";
    public static final String SERVER_TARGET_FOLDER = "target/";
    
    //********************* MESSAGGI BUNDLE ***************************
    //MESSAGGI
    public static final String SPICY_NAME = "++Spicy";
    public static final String ECCEZIONE_SCHEMI = "EccezioneSchemi";
    public static final String CARICAMENTO_SCHEMI_OK = "CaricamentoSchemiOk";
    public static final String CAMPO_NULLO = "CampoNullo";
    public static final String ERROR_DATE_TYPE = "Error_date_type";
    public static final String GENERIC_ERROR = "Generic_error";
    public static final String GENERIC_DELETE = "Generic_delete";
    public static final String GENERIC_WARNING = "Generic_Warning";
    public static final String SYNTAX_WARNING = "Syntax_Warning";
    public static final String CORRUPTED_FILE = "CORRUPTED_FILE";
    public static final String INCLUDE_NODES_WARNING = "INCLUDE_NODES_WARNING";
    public static final String NOT_MODIFIED = "NOT_MODIFIED";
    public static final String NEW_ERROR = "NEW_ERROR";
    public static final String OPEN_ERROR = "OPEN_ERROR";
    public static final String SAVE_ERROR = "SAVE_ERROR";
    public static final String EXPORT_ERROR = "EXPORT_ERROR";
    public static final String CSV_INST_NOTIF="CSV_INST_NOTIF";
    public static final String SAVE_OK = "SAVE_OK";
    public static final String SAVE_ON_CLOSE = "SAVE_ON_CLOSE";
    public static final String CREATE_AUTOMATIC_JOINCONDITION_SOURCE = "CREATE_AUTOMATIC_JOINCONDITION_SOURCE";
    public static final String CREATE_AUTOMATIC_JOINCONDITION_TARGET = "CREATE_AUTOMATIC_JOINCONDITION_TARGET";
    public static final String EXPORT_OK = "EXPORT_OK";
    public static final String ADD_INSTANCE_OK = "ADD_INSTANCE_OK";
    public static final String EMPTY_CORRESPONDENCES = "EMPTY_CORRESPONDENCES";
    public static final String DISCARD_CANDIDATE_CORRESPONDENCES = "DISCARD_CANDIDATE_CORRESPONDENCES";
    public static final String REALLY_CLOSE = "REALLY_CLOSE";
    public static final String CHECK_FOR_MINIMIZE = "CHECK_FOR_MINIMIZE";
    public static final String FILE_EXISTS = "FILE_EXISTS";
    public static final String NOT_MAPPED = "NOT_MAPPED";
    public static final String SINGLE_TRANSFORMATION = "SINGLE_TRANSFORMATION";
    public static final String NOT_LEGAL = "NOT_LEGAL";
    public static final String DELETE_EXCLUSIONE_CORRESPONDENCES = "DELETE_EXCLUSIONE_CORRESPONDENCES";
    public static final String TRANSFORMATION = "TRANSFORMATION";
    public static final String LOAD = "LOAD";
    public static final String LOAD2 = "LOAD2";
    public static final String EXPORT = "EXPORT";
    public static final String APPEND = "APPEND";
    public static final String ADD_TO_LIST = "ADD_TO_LIST";
    public static final String REMOVE_FROM_LIST = "REMOVE_FROM_LIST";
    public static final String WARNING_NOT_TARGET_INSTANCES = "WARNING_NOT_TARGET_INSTANCES";
    public static final String FIND_VALUE_CORRESPONDENCES = "WARNING_FIND_VALUE_CORRESPONDENCES";
    public static final String TOGGLE_MANDATORY = "TOGGLE_MANDATORY";
    public static final String TOGGLE_FOREIGN = "TOGGLE_FOREIGN";
    public static final String DUPLICATION_NO = "DUPLICATION_NO";
    public static final String DELETE_DUPLICATION_NO = "DELETE_DUPLICATION_NO";
    public static final String MESSAGE_NO_RELATIONAL_DATASOURCE = "MESSAGE_NO_RELATIONAL_DATASOURCE";
    public static final String MESSAGE_QUERY_EXECUTED = "MESSAGE_QUERY_EXECUTED";
    public static final String MESSAGE_QUERY_NOT_EXECUTED = "MESSAGE_QUERY_NOT_EXECUTED";
    public static final String NOT_SUPPORTED_EXTENSTION = "NOT_SUPPORTED_EXTENSTION";
    public static final String REFRESH_TGD = "REFRESH_TGD";
    public static final String REFRESH_SQL = "REFRESH_SQL";
    public static final String REFRESH_XQUERY = "REFRESH_XQUERY";
    public static final String INFORMATION_ON_TRAY_START = "INFORMATION_ON_TRAY_START";
    public static final String INFORMATION_ON_TRAY_END_P = "INFORMATION_ON_TRAY_END_P";
    public static final String INFORMATION_ON_TRAY_END_S = "INFORMATION_ON_TRAY_END_S";
    public static final String JFILECHOOSER_FOLDER_FILE_NAME = "JFILECHOOSER_FOLDER_FILE_NAME";
    public static final String JFILECHOOSER_FOLDER_TYPE_FILE = "JFILECHOOSER_FOLDER_TYPE_FILE";
    public static final String NO_INSTANCES_FOR_COMPOSITION = "NO_INSTANCES_FOR_COMPOSITION";
    //MESSAGGI COMPOSIZIONE
    public static final String NOT_ADDED_IN_COMPOSITION = "NOT_ADDED_IN_COMPOSITION";
    public static final String ALREADY_CREATED_COMPOSITION = "ALREADY_CREATED_COMPOSITION";
    public static final String LOAD_DATASOURCE_FOR_CHAIN = "LOAD_DATASOURCE_FOR_CHAIN";
    public static final String DELETE_WIDGET_COMPOSITION = "DELETE_WIDGET_COMPOSITION";
    //MESSAGGI JOIN CONDITION
    public static final String JOIN_DIALOG_TITLE = "JOIN_DIALOG_TITLE";
    public static final String JOIN_DIALOG_MESSAGE = "JOIN_DIALOG_MESSAGE";
    public static final String JOIN_DIALOG_SINGLE = "JOIN_DIALOG_SINGLE";
    public static final String JOIN_DIALOG_MULTIPLE = "JOIN_DIALOG_MULTIPLE";
    public static final String JOIN_DIALOG_CANCEL = "JOIN_DIALOG_CANCEL";
    public static final String JOINCONDITION_DUPLICATE = "JOINCONDITION_DUPLICATE";
    public static final String JOINCONDITION_NO = "JOINCONDITION_NO";
    //MESSAGGI MULTIPL JOIN CONDITION
    public static final String MULTIPLE_JOIN_DIALOG_TITLE = "MULTIPLE_JOIN_DIALOG_TITLE";
    //INFO CONNESSIONI
    public static final String CONNECTION_CONSTRAINT = "connectionConstraint";
    public static final String CONNECTION_CONSTRAINT_TGD = "connectionConstraintTGD";
    public static final String CONNECTION_CONSTRAINT_SPICY = "connectionConstraintSpicy";
    public static final String JOIN_CONNECTION_CONSTRAINT = "joinConnectionConstraint";
    public static final String JOIN_CONNECTION_CONSTRAINT_TGD = "joinConnectionConstraintTGD";
    public static final String JOIN_CONNECTION_CONSTRAINT_SPICY = "joinConnectionConstraintSpicy";
    public static final String JOIN_CONNECTION_CONSTRAINT_SOURCE = "joinConnectionConstraintSource";
    public static final String JOIN_CONNECTION_CONSTRAINT_TARGET = "joinConnectionConstraintTarget";
    public static final String JOIN_CONNECTION_CONSTRAINT_SOURCE_SPICY = "joinConnectionConstraintSourceSpicy";
    public static final String JOIN_CONNECTION_CONSTRAINT_TARGET_SPICY = "joinConnectionConstraintTargetSpicy";
    public static final String SHOW_HIDE_INFO_CONNECTION = "Show_Hide_info_connection";
    public static final String HIDE_INFO_CONNECTION = "Hide_info_connection";
    public static final String DELETE_CONNECTION = "Delete_connection";
    public static final String PROPRIETA_CONNESSIONI = "Connection_properties";
    public static final String IMPLIED = "Implied";
    //INFO CONNESSIONI -- EDIT CONFIDENCE
    public static final String INPUT_TEXT_CONFIDENCE_TITLE = "Input_text_confidence_title";
    public static final String INPUT_TEXT_CONFIDENCE_LABEL = "Input_text_confidence_label";
    public static final String ERROR_LABEL_CONFIDENCE = "Error_label_confidence";
    public static final String TOOL_TIP_CONFIDENCE = "Tool_tip_confidence";
    //EDIT SELECTION CONDITION
    public static final String INPUT_TEXT_SELECTION_CONDITION_TITLE = "INPUT_TEXT_SELECTION_CONDITION_TITLE";
    //ZONA INTERMEDIA
    public static final String CREATE_FUNCTION = "Create_function";
    public static final String CREATE_ATTRIBUTE_GROUP = "Create_Attribute_Group";
    public static final String CREATE_FUNCTIONAL_DEPENDECY = "Create_Functional_Dependency";
    public static final String DELETE_FUNCTIONAL_DEPENDECY = "Delete_Functional_Dependency";
    public static final String CREATE_CONSTANT = "Create_constant";
    public static final String DELETE_INTERMEDIE = "Delete_intermedie";
    //ZONA INTERMEDIA -- DIALOG
    public static final String ADD_FUNCTION="ADD_FUNCTION";
    //ZONA COMPOSIZIONE
    public static final String CREATE_MERGE_WIDGET = "Create_Merge_Widget";
    public static final String CREATE_UNDEFINED_CHAIN_WIDGET = "Create_undefined_chain_widget";
    //ZONA COMPOSIZIONE -- DIALOG
    public static final String INPUT_TEXT_CONSTANT_TITLE = "Input_text_constant_title";
    public static final String INPUT_TEXT_CONSTANT_LABEL = "Input_text_constant_label";
    public static final String INPUT_TEXT_CONSTANT_RADIOS = "Input_text_constant_radio_string";
    public static final String INPUT_TEXT_CONSTANT_RADION = "Input_text_constant_radio_number";
    public static final String INPUT_TEXT_CONSTANT_RADIOF = "Input_text_constant_radio_function";
    public static final String INPUT_TEXT_CONSTANT_FUNCTION1 = "Input_text_constant_function_date";
    public static final String INPUT_TEXT_CONSTANT_FUNCTION2 = "Input_text_constant_function_increment";
    public static final String INPUT_TEXT_CONSTANT_FUNCTION4 = "Input_text_constant_function_increment1";
    //ZONA GLASS-PANE
    public static final String DELETE_ALL_CONNECTIONS = "DELETE_ALL_CONNECTIONS";
    public static final String SET_MULTIPLE_JOIN_SESSION = "SET_MULTIPLE_JOIN_SESSION";
    //Load csv instance
    public static final String TABLE_LABEL="TABLE_LABEL";
    //Export instances to csv
    public static final String EXPORT_COMPLETED_DIALOG="EXPORT_COMPLETED_DIALOG";
    //ZONA SQLDIALOG
    public static final String SQL_DIALOG = "SQL_DIALOG";
    public static final String LABEL_TMP_DATABASE_NAME = "LABEL_TMP_DATABASE_NAME";
    public static final String LABEL_CHECK_RECREATE = "LABEL_CHECK_RECREATE";
    public static final String LABEL_SCHEMA_SOURCE = "LABEL_SCHEMA_SOURCE";
    public static final String LABEL_INSTANCE_SOURCE = "LABEL_INSTANCE_SOURCE";
    public static final String LABEL_SCHEMA_TARGET = "LABEL_SCHEMA_TARGET";
    public static final String INCLUDE_COLUMN_NAMES="INCLUDE_COLUMN_NAMES";
    //EXECUTE SQL
    public static final String INFORMATION_NEEDED = "INFORMATION_NEEDED";
    public static final String LABEL_DRIVER_SUPPORT = "LABEL_DRIVER_SUPPORT";
    public static final String LABEL_URI_SUPPORT = "LABEL_URI_SUPPORT";
    public static final String LABEL_USER_NAME_SUPPORT = "LABEL_USER_NAME_SUPPORT";
    public static final String LABEL_PASSWORD_SUPPORT = "LABEL_PASSWORD_SUPPORT";
    //DIALOG ENGINE CONFIGURATION
    public static final String COMBO_AUTO_SORT = "COMBO_AUTO_SORT";
    public static final String COMBO_SORT = "COMBO_SORT";
    public static final String COMBO_NO_SORT = "COMBO_NO_SORT";
    public static final String COMBO_AUTO_SKOLEM_TABLE = "COMBO_AUTO_SKOLEM_TABLE";
    public static final String COMBO_SKOLEM_TABLE = "COMBO_SKOLEM_TABLE";
    public static final String COMBO_NO_SKOLEM_TABLE = "COMBO_NO_SKOLEM_TABLE";
    //END ENGINE CONFIGURATION
    //VISTE
    public static final String CARICAMENTO_WIZARD_PANEL = "caricamento wizard panel";
    public static final String CARIMENTO_VISUAL_PANEL = "caricamento visual panel";
    public static final String SCHEMI_ALBERI_TOP_COMPONENT = "SCHEMI_ALBERI_TOP_COMPONENT";
    public static final String SCHEMI_ALBERI_TOP_COMPONENT_TOOLTIP = "SCHEMI_ALBERI_TOP_COMPONENT_TOOLTIP";
    public static final String VIEW_SOURCE_INSTANCES_TOP_COMPONENT = "VIEW_SOURCE_INSTANCES_TOP_COMPONENT";
    public static final String VIEW_SOURCE_INSTANCES_TOP_COMPONENT_TOOLTIP = "VIEW_SOURCE_INSTANCES_TOP_COMPONENT_TOOLTIP";
    public static final String VIEW_TRANSFORMATIONS_TOP_COMPONENT = "VIEW_TRANSFORMATIONS_TOP_COMPONENT";
    public static final String VIEW_TRANSFORMATIONS_TOP_COMPONENT_TOOLTIP = "VIEW_TRANSFORMATIONS_TOP_COMPONENT_TOOLTIP";
    public static final String VIEW_SPICY_TOP_COMPONENT = "VIEW_SPICY_TOP_COMPONENT";
    public static final String VIEW_SPICY_TOP_COMPONENT_TOOLTIP = "VIEW_SPICY_TOP_COMPONENT_TOOLTIP";
    public static final String VIEW_BEST_MAPPINGS_TOP_COMPONENT = "VIEW_BEST_MAPPINGS_TOP_COMPONENT";
    public static final String VIEW_BEST_MAPPINGS_TOP_COMPONENT_TOOLTIP = "VIEW_BEST_MAPPINGS_TOP_COMPONENT_TOOLTIP";
    public static final String VIEW_RANKED_TRANSFORMATIONS_TOP_COMPONENT = "VIEW_RANKED_TRANSFORMATIONS_TOP_COMPONENT";
    public static final String VIEW_RANKED_TRANSFORMATIONS_TOP_COMPONENT_TOOLTIP = "VIEW_RANKED_TRANSFORMATIONS_TOP_COMPONENT_TOOLTIP";
    public static final String VIEW_SELECTED_TRANSFORMATION_TOP_COMPONENT = "VIEW_SELECTED_TRANSFORMATION_TOP_COMPONENT";
    public static final String VIEW_SELECTED_TRANSFORMATION_TOP_COMPONENT_TOOLTIP = "VIEW_SELECTED_TRANSFORMATION_TOP_COMPONENT_TOOLTIP";
    public static final String VIEW_GENERATE_XQUERY_TOP_COMPONENT = "VIEW_GENERATE_XQUERY_TOP_COMPONENT";
    public static final String VIEW_GENERATE_XQUERY_TOP_COMPONENT_TOOLTIP = "VIEW_GENERATE_XQUERY_TOP_COMPONENT_TOOLTIP";
    public static final String VIEW_GENERATE_SQL_TOP_COMPONENT = "VIEW_GENERATE_SQL_TOP_COMPONENT";
    public static final String VIEW_GENERATE_SQL_TOP_COMPONENT_TOOLTIP = "VIEW_GENERATE_SQL_TOP_COMPONENT_TOOLTIP";
    public static final String VIEW_PROJECT_TREE_TOP_COMPONENT = "VIEW_PROJECT_TREE_TOP_COMPONENT";
    public static final String VIEW_PROJECT_TREE_TOP_COMPONENT_TOOLTIP = "VIEW_PROJECT_TREE_TOP_COMPONENT_TOOLTIP";
    public static final String VIEW_TGD_LIST_TOP_COMPONENT = "VIEW_TGD_LIST_TOP_COMPONENT";
    public static final String VIEW_TGD_LIST_TOP_COMPONENT_TOOLTIP = "VIEW_TGD_LIST_TOP_COMPONENT_TOOLTIP";
    public static final String VIEW_COMPOSITION_TOP_COMPONENT = "VIEW_COMPOSITION_TOP_COMPONENT";
    public static final String VIEW_COMPOSITION_TOP_COMPONENT_TOOLTIP = "VIEW_COMPOSITION_TOP_COMPONENT_TOOLTIP";
    //COLORS
    public static final Color COLOR_CONNECTION_CONSTRAINT_DEFAULT_CORRESPONDENCE = Color.BLACK;
    public static final Color COLOR_CONNECTION_CONSTRAINT_DEFAULT = Color.GRAY;
    public static final Color COLOR_CONNECTION_CONSTRAINT_SELECTED = Color.RED;
    //BEAN
    public static final String LAST_ACTION_BEAN = "last action bean";
    public static final String ACTUAL_SAVE_FILE = "actual_save_file";
    public static final String CONNECTION_CONSTRAINT_SOURCE = "connectionConstraintSource";
    public static final String CONNECTION_CONSTRAINT_TARGET = "connectionConstraintTarget";
    public static final String CONNECTION_CONSTRAINT_SOURCE_SPICY = "connectionConstraintSourceSpicy";
    public static final String CONNECTION_CONSTRAINT_TARGET_SPICY = "connectionConstraintTargetSpicy";
    public static final String XML_CONFIGURATION_SOURCE = "XML_CONFIGURATION_SOURCE";
    public static final String XML_CONFIGURATION_TARGET = "XML_CONFIGURATION_TARGET";
    public static final String SQL_CONFIGURATION_SOURCE = "SQL_CONFIGURATION_SOURCE";
    public static final String SQL_CONFIGURATION_TARGET = "SQL_CONFIGURATION_TARGET";
    public static final String RELATIONAL_CONFIGURATION_SOURCE = "RELATIONAL_CONFIGURATION_SOURCE";
    public static final String RELATIONAL_CONFIGURATION_TARGET = "RELATIONAL_CONFIGURATION_TARGET";
    public static final String CSV_CONFIGURATION_SOURCE = "CSV_CONFIGURATION_SOURCE";
    public static final String CSV_CONFIGURATION_TARGET = "CSV_CONFIGURATION_TARGET";    
    public static final String NEW_MAPPING_TASK_PM = "NEW_MAPPING_TASK_PM";
    public static final String MAPPINGTASK_SHOWED = "MAPPINGTASK_SHOWED";
    public static final String SCENARIO_MAPPER = "SCENARIO_MAPPER";
//    public static final String MAPPINGTASK = "mappingTask";
    public static final String BEST_MAPPING_TASKS = "bestMappingTasks";
    public static final String RANKED_TRANSFORMATIONS = "rankedTransformations";
    public static final String MAPPINGTASK_SELECTED = "selected mappingTask";
    public static final String SCENARIOS = "SCENARIOS";
    public static final String CURRENT_SCENARIO = "CURRENT_SCENARIO";
    public static final String CONNECTION_SELECTED = "connectionWidgetSelected";
    public static final String CHECK_FIND_BEST_MAPPING = "check find best mapping";
    public static final String LINE_COORDINATES_COLLECTIONS = "LineCoordinatesCollections";
    public static final String CONSTRAINTS_WIDGET_COLLECTIONS = "ConstraintsWidgetCollections";
    public static final String CREATING_JOIN_SESSION = "CREATING_JOIN_SESSION";
    public static final String JOIN_SESSION_SOURCE = "JOIN_SESSION_SOURCE";
    public static final String JOIN_SESSION_TARGET = "JOIN_SESSION_TARGET";
    public static final String JOIN_CONSTRIANTS = "JOIN_CONSTRIANTS";
    public static final String JOIN_CONDITION = "JOIN_CONDITION";
    public static final String FROM_PATH_NODES = "FROM_PATH_NODES";
    public static final String RECREATE_TREE = "RECREATE_TREE";
    public static final String SELECTION_CONDITON_INFO = "SELECTION_CONDITON_INFO";
    public static final String TGD_SESSION = "TGD_SESSION";
    
    public static final String PIN_WIDGET_TREE = "Pin_Tree";
    public static final String PIN_WIDGET_TREE_TGD = "Pin_Tree_TGD";
    public static final String PIN_WIDGET_TREE_SPICY = "Pin_Tree_Spicy";
    public static final int MAX_NODI = 50;
    public static final String DATASOURCE_TYPE_RELATIONAL = "DATASOURCE_TYPE_RELATIONAL";
    public static final String DATASOURCE_TYPE_XML = "DATASOURCE_TYPE_XML";
    public static final String DATASOURCE_TYPE_CSV="DATASOURCE_TYPE_CSV";
    public static final String SOURCE = "Source";
    public static final String TARGET = "Target";
    public static final String SOLUTION = "SOLUTION";
    public static final String MESSAGE_BEST_MAPPINGS = "MESSAGE_BEST_MAPPINGS";
    public static final String MESSAGE_NO_BEST_MAPPINGS = "MESSAGE_NO_BEST_MAPPINGS";
    public static final String MESSAGE_RANKED_TRANSFORMATIONS = "MESSAGE_RANKED_TRANSFORMATIONS";    //TREE TYPE
    public static final String TREE_SOURCE = "source";
    public static final String TREE_TARGET = "target";
    public static final String KEY = "key_type";
    public static final String FOREIGN_KEY = "foreign_key_type";
    public static final String INTERMEDIE = "intermedie";
    public static final String COMPOSITION_TYPE = "composition type";
    public static final String INTERMEDIE_BARRA = "intermedie_barra";    //SPICY
    public static final String RANK = "RANK";
    public static final String QUALITY = "QUALITY";
    public static final String FLUSSO_SPICY = "Spicy";
    public static final String SELECTED_TRANSFORMATION = "SELECTED_TRANSFORMATION";    // ATTENZIONE: COSTANTE DUPLICATA... (Vedi SpicyModelConstants)
    public static final String TYPE_RELATIONAL = "relational";
    public static final String SET_ROOT_IN_COMPOSITION = "SET_ROOT_IN_COMPOSITION";
    //Recommendation, correspondence types
    public static final String SIMPLE_CORRESPONDENCE = "SIMPLE_CONNECTION";
    public static final String EMPTY_CORRESPONDENCE = "EMPTY_CONNECTION";
    public static final String CONSTANT_STRING = "CONSTANT_STRING";
    public static final String CONSTANT_NUMBER = "CONSTANT_NUMBER";
    public static final String CONSTANT_DATE = "CONSTANT_DATE";
    public static final String CONSTANT_DATETIME = "CONSTANT_DATETIME";
    public static final String CONSTANT_SEQUENCE = "CONSTANT_SEQUENCE";
    public static final String CONSTANT_DB_SEQUENCE = "CONSTANT_DB_SEQUENCE";
    public static final String FUNCTION = "FUNCTION_CONNECTION";
}
