// Copyright (C) 2012 jOVAL.org.  All rights reserved.
// This software is licensed under the LGPL 3.0 license available at http://www.gnu.org/licenses/lgpl.txt

package jsaf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;
import ch.qos.cal10n.MessageConveyor;
import ch.qos.cal10n.MessageConveyorException;
import ch.qos.cal10n.MessageParameterObj;

import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

/**
 * Uses cal10n to define localized messages for jSAF.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
@BaseName("jsafmsg")
@LocaleData(
  defaultCharset="ASCII",
  value = { @Locale("en_US") }
)
public enum Message {
    ERROR_AD_BAD_OU,
    ERROR_AD_DOMAIN_REQUIRED,
    ERROR_AD_DOMAIN_UNKNOWN,
    ERROR_AD_INIT,
    ERROR_ADAPTER_MISSING,
    ERROR_ASCII_CONVERSION,
    ERROR_BAD_COMPONENT,
    ERROR_BAD_FILE_OBJECT,
    ERROR_BAD_PLIST_OBJECT,
    ERROR_BAD_PROCESS58_OBJECT,
    ERROR_BAD_TIMEDIFFERENCE,
    ERROR_BINARY_LENGTH,
    ERROR_CHECKSUM_ALGORITHM,
    ERROR_CMDLET,
    ERROR_CMDLET_FIELD,
    ERROR_CMDLET_GUID,
    ERROR_CMDLET_MODULE,
    ERROR_CMDLET_VERB,
    ERROR_CMDLET_VERSION,
    ERROR_COMPONENT_FILTER,
    ERROR_CONFIG_OVERLAY,
    ERROR_CPE_BAD_SOURCE,
    ERROR_DEFINITION_FILTER_BAD_SOURCE,
    ERROR_DEFINITIONS_BAD_SOURCE,
    ERROR_DEFINITIONS_NONE,
    ERROR_DIRECTIVES_BAD_SOURCE,
    ERROR_ENGINE_ABORT,
    ERROR_ENGINE_STATE,
    ERROR_EOF,
    ERROR_EOS,
    ERROR_EXCEPTION,
    ERROR_EXTERNAL_VARIABLE,
    ERROR_EXTERNAL_VARIABLE_SOURCE,
    ERROR_FACTORY_CLASS,
    ERROR_FACTORY_INITIALIZER,
    ERROR_FILE_CLOSE,
    ERROR_FILE_GENERATE,
    ERROR_FILE_SPEC,
    ERROR_FILE_STREAM_CLOSE,
    ERROR_FLAG,
    ERROR_FMRI,
    ERROR_FS_LSTAT,
    ERROR_FS_SEARCH,
    ERROR_FS_SEARCH_LINE,
    ERROR_ILLEGAL_TIME,
    ERROR_INETD_LINE,
    ERROR_INSTANCE,
    ERROR_IO,
    ERROR_IO_NOT_DIR,
    ERROR_IO_NOT_FILE,
    ERROR_IOS_TRAIN_COMPARISON,
    ERROR_LINK_NOWHERE,
    ERROR_LINUX_PARTITION,
    ERROR_MESSAGE_CONVEYOR,
    ERROR_MISSING_RESOURCE,
    ERROR_OBJECT_PERMUTATION,
    ERROR_OVAL,
    ERROR_PASSWD_LINE,
    ERROR_PATTERN,
    ERROR_PE,
    ERROR_PLIST_PARSE,
    ERROR_PLIST_UNSUPPORTED_TYPE,
    ERROR_PLUGIN_CLASSPATH,
    ERROR_PLUGIN_CLASSPATH_ELT,
    ERROR_PLUGIN_INTERFACE,
    ERROR_PLUGIN_MAIN,
    ERROR_POWERSHELL,
    ERROR_POWERSHELL_TIMEOUT,
    ERROR_PROCESS_RETRY,
    ERROR_PROTOCOL,
    ERROR_REF_DEFINITION,
    ERROR_REF_ITEM,
    ERROR_REF_OBJECT,
    ERROR_REF_STATE,
    ERROR_REF_TEST,
    ERROR_REF_VARIABLE,
    ERROR_REFLECTION,
    ERROR_RESOLVE_ITEM_FIELD,
    ERROR_RESOLVE_VAR,
    ERROR_RESULTS_BAD_SOURCE,
    ERROR_RPMINFO,
    ERROR_RPMINFO_SIGKEY,
    ERROR_SC_BAD_SOURCE,
    ERROR_SCE_PLATFORM,
    ERROR_SCE_PLATFORMLANG,
    ERROR_SCHEMATRON_VALIDATION,
    ERROR_SELINUX_BOOL,
    ERROR_SELINUX_SC,
    ERROR_SESSION_CONNECT,
    ERROR_SESSION_INTEGRITY,
    ERROR_SESSION_NONE,
    ERROR_SET_COMPLEMENT,
    ERROR_SHADOW_LINE,
    ERROR_SMF,
    ERROR_SOLPKG,
    ERROR_SUBSTRING,
    ERROR_SYSINFO_ARCH,
    ERROR_SYSINFO_HOSTNAME,
    ERROR_SYSINFO_INTERFACE,
    ERROR_SYSINFO_OSNAME,
    ERROR_SYSINFO_OSVERSION,
    ERROR_SYSINFO_TYPE,
    ERROR_TEST_INCOMPARABLE,
    ERROR_TEST_NOOBJREF,
    ERROR_TESTEXCEPTION,
    ERROR_TIMESTAMP,
    ERROR_TYPE_CONVERSION,
    ERROR_TYPE_INCOMPATIBLE,
    ERROR_UNIX_FILE,
    ERROR_UNIX_FLAVOR,
    ERROR_UNIXFILEINFO,
    ERROR_UNSUPPORTED_CHECK,
    ERROR_UNSUPPORTED_COMPONENT,
    ERROR_UNSUPPORTED_DATATYPE,
    ERROR_UNSUPPORTED_ENTITY,
    ERROR_UNSUPPORTED_EXISTENCE,
    ERROR_UNSUPPORTED_ITEM,
    ERROR_UNSUPPORTED_OBJECT,
    ERROR_UNSUPPORTED_OPERATION,
    ERROR_UNSUPPORTED_OS_VERSION,
    ERROR_UNSUPPORTED_SESSION_TYPE,
    ERROR_UNSUPPORTED_UNIX_FLAVOR,
    ERROR_VARIABLE_MISSING,
    ERROR_VARIABLE_NO_VALUES,
    ERROR_VARIABLES_BAD_SOURCE,
    ERROR_VERSION_CLASS,
    ERROR_VERSION_STR,
    ERROR_WIN_ACCESSTOKEN_PRINCIPAL,
    ERROR_WIN_ACCESSTOKEN_TOKEN,
    ERROR_WIN_AUDITPOL_CODE,
    ERROR_WIN_AUDITPOL_SETTING,
    ERROR_WIN_AUDITPOL_SUBCATEGORY,
    ERROR_WIN_FILESACL,
    ERROR_WIN_LOCKOUTPOLICY_VALUE,
    ERROR_WIN_NOPRINCIPAL,
    ERROR_WIN_SECEDIT_CODE,
    ERROR_WIN_SECEDIT_VALUE,
    ERROR_WIN_WUA_SEARCH,
    ERROR_WINDOWS_BITNESS_INCOMPATIBLE,
    ERROR_WINFILE_OWNER,
    ERROR_WINREG_HIVE,
    ERROR_WINREG_HIVE_NAME,
    ERROR_WINREG_VALUETOSTR,
    ERROR_WINWMI_GENERAL,
    ERROR_XCCDF_MISSING_PART,
    ERROR_XINETD_FILE,
    ERROR_XINETD_FORMAT,
    ERROR_XML_PARSE,
    ERROR_XML_TRANSFORM,
    ERROR_XML_XPATH,
    STATUS_AD_DOMAIN_ADD,
    STATUS_AD_DOMAIN_SKIP,
    STATUS_AD_GROUP_SKIP,
    STATUS_ADAPTER_COLLECTION,
    STATUS_AIX_FILESET,
    STATUS_AIX_FIX,
    STATUS_CHECK_NONE_EXIST,
    STATUS_CONFIG_OVERLAY,
    STATUS_CONFIG_SESSION,
    STATUS_DEFINITION,
    STATUS_EMPTY_ENTITY,
    STATUS_EMPTY_FILE,
    STATUS_EMPTY_RECORD,
    STATUS_EMPTY_SET,
    STATUS_FILTER,
    STATUS_FS_CACHE_RETRIEVE,
    STATUS_FS_CACHE_STORE,
    STATUS_FS_MOUNT_ADD,
    STATUS_FS_MOUNT_SKIP,
    STATUS_FS_SEARCH_CACHE_PROGRESS,
    STATUS_FS_SEARCH_CACHE_TEMP,
    STATUS_FS_SEARCH_DONE,
    STATUS_FS_SEARCH_MATCH,
    STATUS_FS_SEARCH_START,
    STATUS_INETD_NOCONFIG,
    STATUS_INETD_SERVICE,
    STATUS_NAME_DOMAIN_ERR,
    STATUS_NAME_DOMAIN_OK,
    STATUS_NO_PROCESS,
    STATUS_NOT_FILE,
    STATUS_OBJECT,
    STATUS_PE_EMPTY,
    STATUS_PE_READ,
    STATUS_POWERSHELL_EXIT,
    STATUS_POWERSHELL_INVOKE,
    STATUS_POWERSHELL_MODULE_LOAD,
    STATUS_POWERSHELL_MODULE_SKIP,
    STATUS_POWERSHELL_SPAWN,
    STATUS_PROCESS_END,
    STATUS_PROCESS_RETRY,
    STATUS_PROCESS_START,
    STATUS_RPMINFO_LIST,
    STATUS_RPMINFO_RPM,
    STATUS_SESSION_DISPOSE,
    STATUS_SMF,
    STATUS_SMF_SERVICE,
    STATUS_SOLPKG_LIST,
    STATUS_SOLPKG_PKGINFO,
    STATUS_TEST,
    STATUS_UNIX_FILE,
    STATUS_UPN_CONVERT,
    STATUS_VARIABLE_CREATE,
    STATUS_VARIABLE_RECYCLE,
    STATUS_WIN_ACCESSTOKEN,
    STATUS_WIN_WUA,
    STATUS_WINDOWS_BITNESS,
    STATUS_WINREG_VALINSTANCE,
    STATUS_WMI_CONNECT,
    STATUS_WMI_DISCONNECT,
    STATUS_WMI_QUERY,
    STATUS_XINETD_FILE,
    STATUS_XINETD_NOCONFIG,
    STATUS_XINETD_SERVICE,
    WARNING_FIELD_STATUS,
    WARNING_MISSING_OUTPUT,
    WARNING_PERISHABLEIO_INTERRUPT,
    WARNING_WINDOWS_VIEW;

    private static IMessageConveyor baseConveyor;
    private static MultiConveyor conveyor;
    private static LocLoggerFactory loggerFactory;
    private static LocLogger sysLogger;

    static {
	baseConveyor = new MessageConveyor(java.util.Locale.getDefault());
	try {
	    //
	    // Get a message to test whether localized messages are available for the default Locale
	    //
	    baseConveyor.getMessage(ERROR_EXCEPTION);
	} catch (MessageConveyorException e) {
	    //
	    // The test failed, so set the message Locale to English
	    //
	    baseConveyor = new MessageConveyor(java.util.Locale.ENGLISH);
	}
	conveyor = new MultiConveyor();
	loggerFactory = new LocLoggerFactory(conveyor);
	sysLogger = loggerFactory.getLocLogger(Message.class);
    }

    /**
     * Extend Message to be able to provide messages for the specified Enum class, using the specified IMessageConveyor.
     */
    public static void extend(Class<? extends Enum<?>> clazz, IMessageConveyor mc) {
	conveyor.conveyors.put(clazz, mc);
    }

    /**
     * Retrieve the default localized system logger used by the jSAF library.
     */
    public static LocLogger getLogger() {
	return sysLogger;
    }

    /**
     * Retrieve a localized String, given the key and substitution arguments.
     */
    public static String getMessage(Enum<?> key, Object... args) {
	return conveyor.getMessage(key, args);
    }

    public static Set<Map.Entry<Class<? extends Enum>, IMessageConveyor>> getConveyors() {
	return conveyor.conveyors.entrySet();
    }

    // Internal

    /**
     * An IMessageConveyor that consolidates multiple IMessageConveyors.
     */
    static class MultiConveyor implements IMessageConveyor {
	HashMap<Class<? extends Enum>, IMessageConveyor> conveyors;

	MultiConveyor() {
	    conveyors = new HashMap<Class<? extends Enum>, IMessageConveyor>();
	    conveyors.put(Message.class, baseConveyor);
	}

	public <E extends Enum<?>>String getMessage(E key, Object... args) throws MessageConveyorException {
	    IMessageConveyor mc = conveyors.get(key);
	    if (mc == null) {
		throw new MessageConveyorException(baseConveyor.getMessage(ERROR_MESSAGE_CONVEYOR, key.getClass().getName()));
	    } else {
		return mc.getMessage(key, args);
	    }
	}

	public String getMessage(MessageParameterObj mpo) throws MessageConveyorException {
	    return getMessage(mpo.getKey(), mpo.getArgs());
	}
     }
}
