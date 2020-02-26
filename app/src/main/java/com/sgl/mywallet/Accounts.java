
package com.sgl.mywallet;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class Accounts
{
  private String objectId;
  private String userId;
  private Double balance;
  private String ownerId;
  private String type;
  private Date created;
  private Date updated;
  private Double overdraftLimit;

  public Accounts(String userId, String type, Double balance, Double overdraftLimit) {
    this.userId = userId;
    this.balance = balance;
    this.type = type;
    this.overdraftLimit = overdraftLimit;
  }

  public Accounts() {
    this("JSmith", "NoType", 0.0, 0.0);
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public Double getBalance()
  {
    return balance;
  }

  public void setBalance( Double balance )
  {
    this.balance = balance;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public Date getCreated()
  {
    return created;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public Double getOverdraftLimit()
  {
    return overdraftLimit;
  }

  public void setOverdraftLimit( Double overdraftLimit )
  {
    this.overdraftLimit = overdraftLimit;
  }

                                                    
  public Accounts save()
  {
    return Backendless.Data.of( Accounts.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Accounts> callback )
  {
    Backendless.Data.of( Accounts.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Accounts.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Accounts.class ).remove( this, callback );
  }

  public static Accounts findById( String id )
  {
    return Backendless.Data.of( Accounts.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Accounts> callback )
  {
    Backendless.Data.of( Accounts.class ).findById( id, callback );
  }

  public static Accounts findFirst()
  {
    return Backendless.Data.of( Accounts.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Accounts> callback )
  {
    Backendless.Data.of( Accounts.class ).findFirst( callback );
  }

  public static Accounts findLast()
  {
    return Backendless.Data.of( Accounts.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Accounts> callback )
  {
    Backendless.Data.of( Accounts.class ).findLast( callback );
  }

  public static List<Accounts> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Accounts.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Accounts>> callback )
  {
    Backendless.Data.of( Accounts.class ).find( queryBuilder, callback );
  }
}