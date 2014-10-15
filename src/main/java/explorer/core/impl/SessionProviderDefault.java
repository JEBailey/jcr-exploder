package explorer.core.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessControlException;

import javax.jcr.AccessDeniedException;
import javax.jcr.Credentials;
import javax.jcr.InvalidItemStateException;
import javax.jcr.InvalidSerializedDataException;
import javax.jcr.Item;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.LoginException;
import javax.jcr.NamespaceException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.jcr.Workspace;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.retention.RetentionManager;
import javax.jcr.security.AccessControlManager;
import javax.jcr.version.VersionException;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;

import explorer.core.api.SessionProvider;

import org.apache.sling.jcr.api.SlingRepository;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

@Component
@Service
public class SessionProviderDefault implements SessionProvider {

	@Reference
    protected SlingRepository repository;

    protected Session session;

    @Activate
    protected void activate() throws Exception {
        session = repository.loginAdministrative(null);
    }

    @Deactivate
    protected void deactivate() throws Exception {
        session.logout();
        session = null;
    }
	@Override
	public Session getSession(String key) {
		return session;
	}

	@Override
	public void createSession(String name, char[] credentials, String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		try {
			session.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getDefaultWorkspace() {
		return repository.getDefaultWorkspace();
	}

	public String getDescriptor(String arg0) {
		return repository.getDescriptor(arg0);
	}

	public String[] getDescriptorKeys() {
		return repository.getDescriptorKeys();
	}

	public Value getDescriptorValue(String arg0) {
		return repository.getDescriptorValue(arg0);
	}

	public Value[] getDescriptorValues(String arg0) {
		return repository.getDescriptorValues(arg0);
	}

	public boolean isSingleValueDescriptor(String arg0) {
		return repository.isSingleValueDescriptor(arg0);
	}

	public boolean isStandardDescriptor(String arg0) {
		return repository.isStandardDescriptor(arg0);
	}

	public Session login() throws LoginException, RepositoryException {
		return repository.login();
	}

	public Session login(Credentials arg0, String arg1) throws LoginException, NoSuchWorkspaceException,
			RepositoryException {
		return repository.login(arg0, arg1);
	}

	public Session login(Credentials arg0) throws LoginException, RepositoryException {
		return repository.login(arg0);
	}

	public Session login(String arg0) throws LoginException, NoSuchWorkspaceException, RepositoryException {
		return repository.login(arg0);
	}

	public Session loginAdministrative(String arg0) throws LoginException, RepositoryException {
		return repository.loginAdministrative(arg0);
	}

	public Session loginService(String arg0, String arg1) throws LoginException, RepositoryException {
		return repository.loginService(arg0, arg1);
	}

	public void addLockToken(String arg0) {
		session.addLockToken(arg0);
	}

	public void checkPermission(String arg0, String arg1) throws AccessControlException, RepositoryException {
		session.checkPermission(arg0, arg1);
	}

	public void exportDocumentView(String arg0, ContentHandler arg1, boolean arg2, boolean arg3)
			throws PathNotFoundException, SAXException, RepositoryException {
		session.exportDocumentView(arg0, arg1, arg2, arg3);
	}

	public void exportDocumentView(String arg0, OutputStream arg1, boolean arg2, boolean arg3) throws IOException,
			PathNotFoundException, RepositoryException {
		session.exportDocumentView(arg0, arg1, arg2, arg3);
	}

	public void exportSystemView(String arg0, ContentHandler arg1, boolean arg2, boolean arg3)
			throws PathNotFoundException, SAXException, RepositoryException {
		session.exportSystemView(arg0, arg1, arg2, arg3);
	}

	public void exportSystemView(String arg0, OutputStream arg1, boolean arg2, boolean arg3) throws IOException,
			PathNotFoundException, RepositoryException {
		session.exportSystemView(arg0, arg1, arg2, arg3);
	}

	public AccessControlManager getAccessControlManager() throws UnsupportedRepositoryOperationException,
			RepositoryException {
		return session.getAccessControlManager();
	}

	public Object getAttribute(String arg0) {
		return session.getAttribute(arg0);
	}

	public String[] getAttributeNames() {
		return session.getAttributeNames();
	}

	public ContentHandler getImportContentHandler(String arg0, int arg1) throws PathNotFoundException,
			ConstraintViolationException, VersionException, LockException, RepositoryException {
		return session.getImportContentHandler(arg0, arg1);
	}

	public Item getItem(String arg0) throws PathNotFoundException, RepositoryException {
		return session.getItem(arg0);
	}

	public String[] getLockTokens() {
		return session.getLockTokens();
	}

	public String getNamespacePrefix(String arg0) throws NamespaceException, RepositoryException {
		return session.getNamespacePrefix(arg0);
	}

	public String[] getNamespacePrefixes() throws RepositoryException {
		return session.getNamespacePrefixes();
	}

	public String getNamespaceURI(String arg0) throws NamespaceException, RepositoryException {
		return session.getNamespaceURI(arg0);
	}

	public Node getNode(String arg0) throws PathNotFoundException, RepositoryException {
		return session.getNode(arg0);
	}

	public Node getNodeByIdentifier(String arg0) throws ItemNotFoundException, RepositoryException {
		return session.getNodeByIdentifier(arg0);
	}

	public Node getNodeByUUID(String arg0) throws ItemNotFoundException, RepositoryException {
		return session.getNodeByUUID(arg0);
	}

	public Property getProperty(String arg0) throws PathNotFoundException, RepositoryException {
		return session.getProperty(arg0);
	}

	public Repository getRepository() {
		return session.getRepository();
	}

	public RetentionManager getRetentionManager() throws UnsupportedRepositoryOperationException, RepositoryException {
		return session.getRetentionManager();
	}

	public Node getRootNode() throws RepositoryException {
		return session.getRootNode();
	}

	public String getUserID() {
		return session.getUserID();
	}

	public ValueFactory getValueFactory() throws UnsupportedRepositoryOperationException, RepositoryException {
		return session.getValueFactory();
	}

	public Workspace getWorkspace() {
		return session.getWorkspace();
	}

	public boolean hasCapability(String arg0, Object arg1, Object[] arg2) throws RepositoryException {
		return session.hasCapability(arg0, arg1, arg2);
	}

	public boolean hasPendingChanges() throws RepositoryException {
		return session.hasPendingChanges();
	}

	public boolean hasPermission(String arg0, String arg1) throws RepositoryException {
		return session.hasPermission(arg0, arg1);
	}

	public Session impersonate(Credentials arg0) throws LoginException, RepositoryException {
		return session.impersonate(arg0);
	}

	public void importXML(String arg0, InputStream arg1, int arg2) throws IOException, PathNotFoundException,
			ItemExistsException, ConstraintViolationException, VersionException, InvalidSerializedDataException,
			LockException, RepositoryException {
		session.importXML(arg0, arg1, arg2);
	}

	public boolean isLive() {
		return session.isLive();
	}

	public boolean itemExists(String arg0) throws RepositoryException {
		return session.itemExists(arg0);
	}

	public void logout() {
		session.logout();
	}

	public void move(String arg0, String arg1) throws ItemExistsException, PathNotFoundException, VersionException,
			ConstraintViolationException, LockException, RepositoryException {
		session.move(arg0, arg1);
	}

	public boolean nodeExists(String arg0) throws RepositoryException {
		return session.nodeExists(arg0);
	}

	public boolean propertyExists(String arg0) throws RepositoryException {
		return session.propertyExists(arg0);
	}

	public void refresh(boolean arg0) throws RepositoryException {
		session.refresh(arg0);
	}

	public void removeItem(String arg0) throws VersionException, LockException, ConstraintViolationException,
			AccessDeniedException, RepositoryException {
		session.removeItem(arg0);
	}

	public void removeLockToken(String arg0) {
		session.removeLockToken(arg0);
	}

	public void setNamespacePrefix(String arg0, String arg1) throws NamespaceException, RepositoryException {
		session.setNamespacePrefix(arg0, arg1);
	}

}
