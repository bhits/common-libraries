/*******************************************************************************
 * Open Behavioral Health Information Technology Architecture (OBHITA.org)
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the <organization> nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package gov.samhsa.c2s.common.audit;

import ch.qos.logback.audit.Application;
import ch.qos.logback.audit.AuditException;
import ch.qos.logback.audit.client.AuditorFacade;
import ch.qos.logback.audit.client.AuditorFactory;
import ch.qos.logback.audit.client.ImprovedAuditorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class AuditClientImpl.
 */
public class AuditClientImpl implements AuditClient {

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The application name.
     */
    private String applicationName;

    private String host;

    private int port;

    /**
     * Instantiates a new audit client.
     * This constructor depends on a <code>applicationName/logback-audit.xml</code>
     * file in the classpath for host and port configuration.
     *
     * @param applicationName the application name
     * @throws AuditException the audit exception
     */
    public AuditClientImpl(String applicationName) throws AuditException {
        super();
        this.applicationName = applicationName;
        this.host = null;
        this.port = 0;
    }

    /**
     * Instantiates a new audit client.
     * This constructor does not depend on any configuration files in the classpath.
     * It uses the constructor arguments {@link AuditClientImpl#host} and {@link AuditClientImpl#port}
     * to configure the audit client.
     *
     * @param applicationName the application name
     * @param host the host for the audit server
     * @param port the port that the audit server listens to
     * @throws AuditException
     */
    public AuditClientImpl(String applicationName, String host, int port) throws AuditException {
        super();
        this.applicationName = applicationName;
        this.host = host;
        this.port = port;
    }

    @Override
    public void audit(Object auditingObject, String subject, AuditVerb verb,
                      String object, Map<PredicateKey, String> predicateMap)
            throws AuditException {
        AuditorFacade af = createAuditorFacade(subject, verb, object);
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("Cannot find the host of auditingObject.");
            logger.error(e.getMessage(), e);
        }

        Application app = createApplication(auditingObject, hostAddress);

        af.originating(app);
        af.setPredicateMap(mapKeyToString(predicateMap));
        af.audit();
    }

    @Override
    public Map<PredicateKey, String> createPredicateMap() {
        return new HashMap<>();
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    @PostConstruct
    public void init() throws AuditException {
        if (StringUtils.hasText(host) && port > 0) {
            ImprovedAuditorFactory.setApplicationNameWithHostAndPort(applicationName, host, port);
        } else {
            AuditorFactory.setApplicationName(applicationName);
        }
    }

    @Override
    @PreDestroy
    public void destroy() {
        AuditorFactory.reset();
    }

    /**
     * Creates the application.
     *
     * @param auditingObject the auditing object
     * @param hostAddress    the host address
     * @return the application
     */
    Application createApplication(Object auditingObject, String hostAddress) {
        Application app;
        if (auditingObject instanceof String)
            app = new Application((String) auditingObject,
                    hostAddress);
        else {
            app = new Application(auditingObject.getClass().getName(),
                    hostAddress);
        }
        return app;
    }

    /**
     * Creates the auditor facade.
     *
     * @param subject the subject
     * @param verb    the verb
     * @param object  the object
     * @return the auditor facade
     */
    AuditorFacade createAuditorFacade(String subject, AuditVerb verb,
                                      String object) {
        AuditorFacade af = new AuditorFacade(subject, verb.getAuditVerb(),
                object);
        return af;
    }

    /**
     * Map key to string.
     *
     * @param predicateMap the predicate map
     * @return the map
     */
    private Map<String, String> mapKeyToString(
            Map<PredicateKey, String> predicateMap) {
        Map<String, String> stringMap = null;
        if (predicateMap != null) {
            stringMap = new HashMap<>();
            for (PredicateKey key : predicateMap.keySet()) {
                stringMap.put(key.getPredicateKey(), predicateMap.get(key));
            }
        }
        return stringMap;
    }
}
