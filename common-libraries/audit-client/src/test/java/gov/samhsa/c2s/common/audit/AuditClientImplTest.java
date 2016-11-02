package gov.samhsa.c2s.common.audit;

import ch.qos.logback.audit.AuditException;
import ch.qos.logback.audit.client.AuditorFacade;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuditClientImplTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private String applicationNameMock = "applicationNameMock";
    private String hostMock = "hostMock";
    private int portMock = 8080;

    @InjectMocks
    private AuditClientImpl sut = new AuditClientImpl(applicationNameMock, hostMock, portMock);

    public AuditClientImplTest() throws AuditException {
    }

    @Test
    public void testAudit() throws AuditException {
        // Arrange
        AuditClientImpl spy = spy(sut);
        Object auditingObjectMock = this;
        String subjectMock = "subjectMock";
        AuditVerb auditVerbMock = () -> "DEPLOY_POLICY";
        String objectMock = "objectMock";
        Map<PredicateKey, String> predicateMapMock = new HashMap<PredicateKey, String>();
        AuditorFacade afMock = mock(AuditorFacade.class);
        doReturn(afMock).when(spy).createAuditorFacade(subjectMock,
                auditVerbMock, objectMock);

        // Act
        spy.audit(auditingObjectMock, subjectMock, auditVerbMock, objectMock,
                predicateMapMock);

        // Assert
        verify(afMock, times(1)).audit();
    }
}