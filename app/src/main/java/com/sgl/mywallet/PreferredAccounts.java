
package com.sgl.mywallet;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class PreferredAccounts
{
  private Date updated;
  private Double posInterest;
  private Double negInterest;
  private Date created;
  private String objectId;
  private String ownerId;
  private String accountId;

  public PreferredAccounts(String accountId, Double posInterest, Double negInterest) {
    this.posInterest = posInterest;
    this.negInterest = negInterest;
    this.accountId = accountId;
  }

  public PreferredAccounts() {
    this("No Account", 0.0, 0.0);
  }

  public Date getUpdated()
  {
    return updated;
  }

  public Double getPosInterest()
  {
    return posInterest;
  }

  public void setPosInterest( Double posInterest )
  {
    this.posInterest = posInterest;
  }

  public Double getNegInterest()
  {
    return negInterest;
  }

  public void setNegInterest( Double negInterest )
  {
    this.negInterest = negInterest;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public String getAccountId()
  {
    return accountId;
  }

  public void setAccountId( String accountId )
  {
    this.accountId = accountId;
  }

                                                    
  public PreferredAccounts save()
  {
    return Backendless.Data.of( PreferredAccounts.class ).save( this );
  }

  public void saveAsync( AsyncCallback<PreferredAccounts> callback )
  {
    Backendless.Data.of( PreferredAccounts.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( PreferredAccounts.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( PreferredAccounts.class ).remove( this, callback );
  }

  public static PreferredAccounts findById( String id )
  {
    return Backendless.Data.of( PreferredAccounts.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<PreferredAccounts> callback )
  {
    Backendless.Data.of( PreferredAccounts.class ).findById( id, callback );
  }

  public static PreferredAccounts findFirst()
  {
    return Backendless.Data.of( PreferredAccounts.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<PreferredAccounts> callback )
  {
    Backendless.Data.of( PreferredAccounts.class ).findFirst( callback );
  }

  public static PreferredAccounts findLast()
  {
    return Backendless.Data.of( PreferredAccounts.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<PreferredAccounts> callback )
  {
    Backendless.Data.of( PreferredAccounts.class ).findLast( callback );
  }

  public static List<PreferredAccounts> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( PreferredAccounts.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<PreferredAccounts>> callback )
  {
    Backendless.Data.of( PreferredAccounts.class ).find( queryBuilder, callback );
  }
}