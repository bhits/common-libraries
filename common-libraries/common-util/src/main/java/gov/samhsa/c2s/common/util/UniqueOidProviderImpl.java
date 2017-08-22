package gov.samhsa.c2s.common.util;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class UniqueOidProviderImpl.
 */
public class UniqueOidProviderImpl implements UniqueOidProvider {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UniqueOidProviderImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see UniqueOidProvider#getOid()
	 */
	@Override
	public String getOid() {
		UUID uuid = UUID.randomUUID();
		String id = String.valueOf(uuid);

		id = id.replace("-", ".");

		id = id.replace("a", "10");
		id = id.replace("b", "11");
		id = id.replace("c", "12");
		id = id.replace("d", "13");
		id = id.replace("e", "14");
		id = id.replace("f", "15");

		// Removes leading zeroes
		id = id.replaceFirst("^0+(?!$)", "");

		return id;
	}
}
